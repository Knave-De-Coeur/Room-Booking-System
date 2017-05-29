package com.objects;

public class Module {
    
    private int id;
    private String moduleName;
    private String moduleDesc;
    private String moduleLeader;
    private String username;

    public Module(String moduleName, String moduleDesc, String moduleLeader, String username) {
        this.id = id;
        this.moduleName = moduleName;
        this.moduleDesc = moduleDesc;
        this.moduleLeader = moduleLeader;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

    public String getModuleLeader() {
        return moduleLeader;
    }

    public void setModuleLeader(String moduleLeader) {
        this.moduleLeader = moduleLeader;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Module{" + "id=" + id + ", moduleName=" + moduleName + ", moduleDesc=" + moduleDesc + ", moduleLeader=" + moduleLeader + ", username=" + username + '}';
    }
}
