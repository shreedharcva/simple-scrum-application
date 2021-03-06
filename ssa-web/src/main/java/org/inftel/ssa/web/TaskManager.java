package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.TaskStatus;
import org.inftel.ssa.services.ResourceService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean
@SessionScoped
public class TaskManager implements Serializable {

	private final static Logger logger = Logger.getLogger(TaskManager.class.getName());
	@EJB
	private ResourceService resources;
	@ManagedProperty(value = "#{sprintManager}")
	private SprintManager sprintManager;
	@ManagedProperty(value = "#{projectManager}")
	private ProjectManager projectManager;
	@ManagedProperty(value = "#{userManager}")
	private UserManager userManager;
	private static final long serialVersionUID = 1L;
	private Task currentTask;
	private boolean accepted;
	private LazyDataModel<Task> tasks = new LazyDataModel() {

		@Override
		public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
			logger.log(Level.INFO, "lazy data model [first={0}, pageSize={1}, sortField={2}, sortOrder={3}, filters={4}]", new Object[]{first, pageSize, sortField, sortOrder, filters});
			setRowCount(resources.countTasksByProject(projectManager.getCurrentProject(), sortField, sortOrder == SortOrder.ASCENDING, filters));
			return resources.findTaksByProject(projectManager.getCurrentProject(), first, pageSize, sortField, sortOrder == SortOrder.ASCENDING, filters);
		}
	};

	public String create() {
		Task task = new Task();
		task.setStatus(TaskStatus.TODO);
		task.setProject(projectManager.getCurrentProject());
		setCurrentTask(task);
		return "/task/create?faces-redirect=true";

	}

	public String save() {
		if (currentTask != null) {
			resources.saveTask(currentTask);
		}
		return "/task/index?faces-redirect=true";
	}

	public void remove() {
	}

	public String edit() {
		setCurrentTask(tasks.getRowData());
		accepted = tasks.getRowData().getUser() != null;
		return "/task/edit?faces-redirect=true";
	}
	
	// --------------------------------------------------------------------------- Getters & Setters
	
	public SprintManager getSprintManager() {
		return sprintManager;
	}

	public void setSprintManager(SprintManager sprintManager) {
		this.sprintManager = sprintManager;
	}

	public LazyDataModel<Task> getTasks() {
		return tasks;
	}

	public void setTasks(LazyDataModel<Task> tasks) {
		this.tasks = tasks;
	}

	public ProjectManager getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(ProjectManager projectManager) {
		this.projectManager = projectManager;
	}

	public Task getCurrentTask() {
		return currentTask;
	}

	public void setCurrentTask(Task currentTask) {
		this.currentTask = currentTask;
	}

	public boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		currentTask.setUser(getUserManager().getCurrentUser());
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public int getTaskStatus() {
		return currentTask.getStatus().ordinal();
	}

	public void setTaskStatus(int taskStatus) {
		currentTask.setStatus(TaskStatus.values()[taskStatus]);
	}
}
