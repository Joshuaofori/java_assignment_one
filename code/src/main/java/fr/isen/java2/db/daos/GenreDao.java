package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;



import static fr.isen.java2.db.daos.DataSourceFactory.getDataSource;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		
		List<Genre> listOfGenre = new ArrayList<>();
		
	    try (Connection connection = getDataSource().getConnection()) {
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM genre")
	            		) {
	                while (results.next()) {
	                	listOfGenre.add(new Genre(results.getInt("idgenre"),results.getString("name")));
	                   
	                }
	                statement.close();
	            }
	        }
	        connection.close();
	    } catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	        
	    }
	    return listOfGenre;
		
	}

	public Genre getGenre(String name) {
		 try (Connection connection = getDataSource().getConnection()) {
		        try (PreparedStatement statement = connection.prepareStatement(
		                    "SELECT * FROM genre WHERE name =?")) {
		            statement.setString(1, name);
		            try (ResultSet results = statement.executeQuery()) {
		                if (results.next()) {
		                    return new Genre(results.getInt("idgenre"),results.getString("name"));
		                }
		            }
		            statement.close();
		        }
		        connection.close();
		    } catch (SQLException e) {
		        // Manage Exception
		        e.printStackTrace();
		    }
		    return null;
	}

	public void addGenre(String name) {
	
		try (Connection connection = getDataSource().getConnection()) {
	        String sqlQuery = "INSERT INTO genre(name) VALUES(?)";
	        try (PreparedStatement statement = connection.prepareStatement(
	                        sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, name);
	            statement.executeUpdate();
	            ResultSet ids = statement.getGeneratedKeys();
	            statement.close();  
	        }
	        connection.close();
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
		
	  
	}
}
