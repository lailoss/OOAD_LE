public abstract class User {
    protected String userId;
    protected String name;
    protected String role;
    
    public User(String userId, String name, String role) {
        this.userId = userId;
        this.name = name;
        this.role = role;
    }
    
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getRole() { return role; }
    
    public void login() {
        System.out.println(role + " " + name + " logged in.");
    }
    
    @Override
    public String toString() {
        return name + " (" + role + ")";
    }
}