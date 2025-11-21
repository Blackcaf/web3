package com.nlshakal.jsfgraph;

import com.nlshakal.jsfgraph.db.DAOFactory;
import com.nlshakal.jsfgraph.entity.ResultEntity;
import com.nlshakal.jsfgraph.mbeans.*;
import com.nlshakal.jsfgraph.utils.AreaChecker;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Locale;

import javax.management.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class ResultsControllerBean implements Serializable {
    private XCoordinateBean xCoordinateBean;
    private YCoordinateBean yCoordinateBean;
    private RCoordinateBean rCoordinateBean;
    private PointsCounterMBean pointsCounter;
    private MissPercentageMBean missPercentage;

    private ArrayList<ResultEntity> results = new ArrayList<>();

    @PostConstruct
    public void init() {
        var resultsEntities = DAOFactory.getInstance().getResultDAO().getAllResults();
        results = new ArrayList<>(resultsEntities);
        log.info("Results initialized with {} entries.", results.size());
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName pointsCounterName = new ObjectName("com.nlshakal.jsfgraph.mbeans:type=PointsCounter");
            pointsCounter = new PointsCounter(DAOFactory.getInstance().getResultDAO());
            mbs.registerMBean(pointsCounter, pointsCounterName);

            NotificationListener listener = (notification, handback) -> System.out.println("Received notification: " + notification.getMessage());
            mbs.addNotificationListener(pointsCounterName, listener, null, null);

            missPercentage = new MissPercentage(pointsCounter);
            ObjectName missPercentageName = new ObjectName("com.nlshakal.jsfgraph.mbeans:type=MissPercentage");
            StandardMBean missPercentageMBean = new StandardMBean(missPercentage, MissPercentageMBean.class);
            mbs.registerMBean(missPercentageMBean, missPercentageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addResult(Double x, Double y, Double r) {
        boolean result = AreaChecker.isInArea(x, y, r);
        if (result) {
            pointsCounter.incrementTotalPoints();
            pointsCounter.resetConsecutiveMisses();
        } else {
            pointsCounter.incrementTotalPoints();
            pointsCounter.incrementMissedPoints();
        }

        ResultEntity en = ResultEntity.builder().x(x).y(y).r(r).result(result).build();

        results.add(en);
        DAOFactory.getInstance().getResultDAO().addNewResult(en);
        log.info("Added new result to the db: X={}, Y={}, R={}", x, y, r);

        String script = String.format(
                Locale.US, "window.drawDotOnCanvas(%f, %f, %f, %b, true);", x, y, r,
                result);
        FacesContext.getCurrentInstance()
                .getPartialViewContext()
                .getEvalScripts()
                .add(script);
    }

    public void updateCanvas(double r) {
        for (ResultEntity en : results) {
            boolean result = AreaChecker.isInArea(en.getX(), en.getY(), r);
            en.setR(r);
            en.setResult(result);

            String script = String.format(
                    Locale.US, "window.drawDotOnCanvas(%f, %f, %f, %b, true);",
                    en.getX(), en.getY(), r, result);
            System.out.println("Script: " + script);
            FacesContext.getCurrentInstance()
                    .getPartialViewContext()
                    .getEvalScripts()
                    .add(script);
        }
        log.info("Canvas updated with new radius: {}", r);
    }

    public void clearResults() {
        DAOFactory.getInstance().getResultDAO().clearResults();
        results.clear();
        pointsCounter.resetAndInitializeCounts();
        log.info("All results cleared from database and local array.");
    }
}
