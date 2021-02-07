package fr.isen.java2.db.daos;

import static fr.isen.java2.db.daos.DataSourceFactory.getDataSource;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Film;
import fr.isen.java2.db.entities.Genre;

public class FilmDao {
String url="jdbc:sqlite:sqlite.db";

/**
 * This method listFilm()
 * returns the list of film
 * select all from film join genre
 * list of film is added to the list 
 * @return
 */
	public List<Film> listFilms() {
		
List<Film> listOfFilm = new ArrayList<>();
		
	    try (Connection connection = DriverManager.getConnection(url)) {//DriverManager bonus point try
	        try (Statement statement = connection.createStatement()) {
	            try (ResultSet results = statement.executeQuery("SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre")) {
	                while (results.next()) {
	                	listOfFilm.add(new Film(results.getInt("idfilm"),
	                            results.getString("title"),
	                            results.getDate("release_date").toLocalDate(),
	                            new Genre(results.getInt("genre_id"),results.getString("name")),
	                            results.getInt("duration"),
	                            results.getString("director"),
	                            results.getString("summary")));  
	                	
	                }	
	            }
	            statement.close();
	           
	        }
	        connection.close();
	    } catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
	    return listOfFilm;
	}
/**
 * listFilmsByGenre() pass an argument to select the film with the desired argument
 * @param genreName
 * @return
 */
	public List<Film> listFilmsByGenre(String genreName) {
		List<Film> listOfFilm = new ArrayList<>();
		 try (Connection connection = getDataSource().getConnection()) {
		        try (PreparedStatement statement = connection.prepareStatement(
		                    "SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name =?")) {
		            statement.setString(1, genreName);
		            try (ResultSet results = statement.executeQuery()) {
		                while(results.next()) {
		                    listOfFilm.add(new Film(results.getInt("idfilm"),
	                            results.getString("title"),
	                            results.getDate("release_date").toLocalDate(),
	                            new Genre(results.getInt("genre_id"),results.getString("name")),
	                            results.getInt("duration"),
	                            results.getString("director"),
	                            results.getString("summary")));
		                }
		            }
		            statement.close();
		        }
		        connection.close();
		    } catch (SQLException e) {
		        // Manage Exception
		        e.printStackTrace();
		    }
		    return listOfFilm;
		    //SELECT * FROM film JOIN genre ON film.genre_id = genre.idgenre WHERE genre.name = 'Comedy'
		    //SELECT * FROM genre WHERE name =?
	}

	/**
	 * adds a film to the film database using the sql query
	 * returns the added film as a return type
	 * @param film
	 * @return
	 */
	public Film addFilm(Film film) {
		try (Connection connection = getDataSource().getConnection()) {
	        String sqlQuery = "INSERT INTO film(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)";
	        try (PreparedStatement statement = connection.prepareStatement(
	                        sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
	            statement.setString(1, film.getTitle());
	            statement.setDate(2,Date.valueOf(film.getReleaseDate()));
	            statement.setInt(3,film.getGenre().getId() );
	            statement.setInt(4, film.getDuration());
	            statement.setString(5,film.getDirector());
	            statement.setString(6, film.getSummary());
	            statement.executeUpdate();
	            ResultSet ids = statement.getGeneratedKeys();
	            if (ids.next()) {
	                return new Film(ids.getInt(1),film.getTitle(),film.getReleaseDate(),film.getGenre(),film.getDuration(),film.getDirector(),
	                		film.getSummary());
	            }
	           
	            statement.close();
	        }
	        connection.close();
	    }catch (SQLException e) {
	        // Manage Exception
	        e.printStackTrace();
	    }
		return null;
	}
}
