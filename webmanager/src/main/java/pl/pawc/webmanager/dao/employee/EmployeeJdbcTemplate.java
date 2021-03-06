package pl.pawc.webmanager.dao.employee;

import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import pl.pawc.webmanager.model.Employee;

public class EmployeeJdbcTemplate implements EmployeeDAO{
	
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplateObject;
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
		this.jdbcTemplateObject = new JdbcTemplate(dataSource);
	}

	public List<Employee> getEmployees(){
		String SQL = "select * from employees;";
		List<Employee> result = jdbcTemplateObject.query(SQL, new EmployeeMapper());
		return result;
	}

	public int insertEmployee(String firstName, String lastName, String birthDate, String employedSince, String department, String superior){
		String login = firstName.toLowerCase()+"."+lastName.toLowerCase();
		String SQL = "insert into employees(login, firstName, lastName, birthDate, employedSince, stillEmployed, department, superior)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?)";
		int rowsAffected = jdbcTemplateObject.update(SQL, login, firstName, lastName, birthDate, employedSince, 1, department, superior);
		return rowsAffected;
	}
	
	public int editEmployee(String firstName, String lastName, String birthDate, String employedSince, String stillEmployed, String department, String superior, String login){
		String SQL = "update employees set firstName=?, lastName=?, birthDate=?, employedSince=?, stillEmployed=?, department=?, superior=? where login=?;";
		int rowsAffected = jdbcTemplateObject.update(SQL, firstName, lastName, birthDate, employedSince, stillEmployed, department, superior, login);
		return rowsAffected;
	}
	
	public int deleteEmployees(Set<String> employees){
		String SQL = "delete from employees where login=?";
		int rowsAffected = 0;
		for(String login : employees){
			rowsAffected += jdbcTemplateObject.update(SQL, login);
		}
		return rowsAffected;
	}

	public Employee getEmployee(String login) {
		String SQL = "select * from employees where login=?";
		Employee employee = jdbcTemplateObject.query(SQL, new Object[]{login}, new EmployeeMapper()).get(0);
		return employee;
	}

}