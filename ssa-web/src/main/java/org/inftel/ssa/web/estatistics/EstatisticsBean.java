package org.inftel.ssa.web.estatistics;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import org.inftel.ssa.datamining.DataminingData;
import org.inftel.ssa.datamining.DataminingDataPeriod;
import org.inftel.ssa.datamining.DataminingProcessor;
import org.inftel.ssa.domain.Project;
import org.inftel.ssa.domain.Sprint;
import org.inftel.ssa.domain.Task;
import org.inftel.ssa.domain.User;
import org.inftel.ssa.web.ProjectManager;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.primefaces.model.chart.PieChartModel;

@ManagedBean
@RequestScoped
public class EstatisticsBean implements Serializable {

    @EJB
    private DataminingProcessor datamining;
    private CartesianChartModel stressModel;
    private CartesianChartModel taskModel;
    private CartesianChartModel individualModel;
    private PieChartModel pieTaskModel;
    
    @ManagedProperty(value = "#{projectManager}")
    private ProjectManager projectManager;

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public EstatisticsBean() {
        createStressModel();
        createTaskModel();
        createPieTaskModel();
       // createIndividualModel();
    }

    public CartesianChartModel getEsfuerzoModel() {
        return stressModel;
    }

    public CartesianChartModel getIndividualModel() {
        return individualModel;
    }

    public CartesianChartModel getTareasModel() {
        return taskModel;
    }
    
    public PieChartModel getPieTaskModel() {
        return pieTaskModel;
    }

    private void createStressModel() {
        stressModel = new CartesianChartModel();
        LineChartSeries series = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha

        String name = "task." + "id_usuario" + "." + "sprint" + ".remaining"; // Con id_usuario y sprint pasádo por parámetro
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            series.set(df.format(date), samples.get(date).getDataSum());
        }

        stressModel.addSeries(series);

    }

    private void createTaskModel() {

        taskModel = new CartesianChartModel();
        LineChartSeries series = new LineChartSeries();
        Map<Date, DataminingData> samples; // todos los datos por fecha

        String name = "task." + "id_usuario" + "." + "sprint" + ".remaining"; // Con id_usuario y sprint pasádo por parámetro
        samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

        for (Date date : samples.keySet()) {
            series.set(df.format(date), samples.get(date).getDataCount());
        }

        taskModel.addSeries(series);

    }
    
    private void createPieTaskModel() {
        pieTaskModel = new PieChartModel();
        int todocount = 0, doingcount = 0, donecount = 0;
        List<Task> tasks = getProjectManager().getCurrentProject().getTasks();
        for (Task task : tasks) {
            switch (task.getStatus()) {
                case TODO:
                    todocount++;
                    break;
                case DOING:
                    doingcount++;
                    break;
                case DONE:
                    donecount++;
                    break;
            }
        }

        pieTaskModel.set("To Do", todocount);
        pieTaskModel.set("Doing", doingcount);
        pieTaskModel.set("Done", donecount);
    }

  /*  private void createIndividualModel() {

        individualModel = new CartesianChartModel();
        new Project().getUsers();
        List<User> listUsers = project.getUsers();  //Con idSprint el id del Sprint 
        Iterator iter = listUsers.iterator(); 
        while (iter.hasNext()) {


            LineChartSeries series = new LineChartSeries();
            series.setLabel(iter.nickname); //Con nickname el nombre del Usuario


 
            Map<Date, Long> samples; // todos los datos por fecha
            String name = "task." + "id_usuario" + "." + "sprint" + ".remaining";
            samples = datamining.findStatistics(name, DataminingDataPeriod.DAYLY, new Date(0), new Date());
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, new Locale("es"));

            for (Date date : samples.keySet()) {
                series.set(df.format(date), samples.get(date));
            }

            individualModel.addSeries(series);
        }
    }  */
}
 