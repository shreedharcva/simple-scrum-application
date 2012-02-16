/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inftel.ssa.web;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.services.ResourceService;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author Jesus Ruiz Oliva
 */
@ManagedBean
@SessionScoped
public class ProjectManager implements Serializable {

    @EJB
    private ResourceService resources;
    private static final long serialVersionUID = 8799656478674718638L;
    private LazyDataModel<Project> projects;
    private Project currentProject;

    public ProjectManager() {
        super();
        projects = new LazyDataModel() {

            @Override
            public List load(int first, int pageSize, String sortField, org.primefaces.model.SortOrder sortOrder, Map filters) {
                
                return resources.findProjects(first,pageSize,sortField,sortOrder.equals(SortOrder.ASCENDING),filters);
            }

            @Override
            public void setRowIndex(int rowIndex) {
                /*
                 * The following is in ancestor (LazyDataModel): this.rowIndex =
                 * rowIndex == -1 ? rowIndex : (rowIndex % pageSize);
                 */
                if (rowIndex == -1 || getPageSize() == 0) {
                    super.setRowIndex(-1);
                } else {
                    super.setRowIndex(rowIndex);
                }
            }
        };

    }

    public LazyDataModel<Project> getProjects() {
        projects.setRowCount(resources.countProjects());

        return projects;
    }

    public void setProjects(LazyDataModel<Project> projects) {
        this.projects = projects;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(Project currentProject) {
        this.currentProject = currentProject;
    }

    public String showSprints() {
        setCurrentProject(projects.getRowData());
        return "/sprint/show.xhtml";
    }

    public String remove() {
        Project project = projects.getRowData();
        resources.removeProject(project);      
        return "/project/show.xhtml";
    }

    public String create() {
        Project medico = new Project();
        setCurrentProject(medico);
        return "/project/create.xhtml";
    }

    public String save() {
        if (currentProject != null) {
            resources.saveProject(currentProject);
        }
        return "/project/show.xhtml";
    }
    public String edit(){
        setCurrentProject(projects.getRowData());
        return "/project/edit.xhtml";
    }
    public String cancelEdit(){
        return "/project/show.xhtml";
    }
}