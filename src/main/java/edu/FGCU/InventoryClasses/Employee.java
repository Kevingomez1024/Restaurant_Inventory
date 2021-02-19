package edu.FGCU.InventoryClasses;

public class Employee{
    private int employeeId;
    private String employeeName;
    private String employeePosition;
    private String password;


    public Employee(int employeeId, String employeeName, String employeePosition,String password){
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeePosition = employeePosition;
        this.password = password;
    }

    public Employee(String[] employeeObject){
        this.employeeId = Integer.parseInt(employeeObject[0]);
        this.employeeName = employeeObject[1];
        this.employeePosition = employeeObject[2];
        this.password = employeeObject[3];
    }

    @Override
    public String toString(){
        return (employeeId + "," + employeeName+ "," +employeePosition+","+password).toLowerCase();
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getPosition() {
        return employeePosition;
    }

    public void setPosition(String position) {
        this.employeePosition = position;
    }

}
