package com.jbpm.util;
import org.jbpm.api.Configuration;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.IdentityService;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskService;
public class JbpmUtil {
    private static ProcessEngine processEngine; 
    private static RepositoryService repositoryService; 
    private static ExecutionService executionService; 
    private static IdentityService identityService; 
    private static TaskService taskService; 
     
    /**
     * 初始化processEngine
     * @return
     */
    public static ProcessEngine getProcessEngine() { 
        if (processEngine == null) { 
            Configuration config = new Configuration(); 
            processEngine = config.buildProcessEngine(); 
        } 
        return processEngine; 
    } 
    
    /**
     * 初始化RepositoryService
     * @return
     */
    public static RepositoryService getRepositoryService() { 
        if (repositoryService == null) repositoryService = getProcessEngine().getRepositoryService(); 
        return repositoryService; 
    } 
    
    /**
     * 初始化ExecutionService
     * @return
     */
    public static ExecutionService getExecutionService() { 
        if (executionService == null) executionService = getProcessEngine().getExecutionService(); 
        return executionService; 
    } 
    
    /**
     * 初始化IdentityService
     * @return
     */
    public static IdentityService getIdentityService() { 
        if (identityService == null) identityService = getProcessEngine().getIdentityService(); 
        return identityService; 
    } 
    
    /**
     * 初始化TaskService
     * @return
     */
    public static TaskService getTaskService() { 
        if (taskService == null) taskService = getProcessEngine().getTaskService(); 
        return taskService; 
    } 
}

