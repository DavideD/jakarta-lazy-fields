/* Hibernate, Relational Persistence for Idiomatic Java
 *
 * SPDX-License-Identifier: Apache-2.0
 * Copyright: Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.reactive.example.session;

import java.time.LocalDate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static java.lang.System.out;
import static java.time.Month.JANUARY;
import static java.time.Month.JUNE;
import static java.time.Month.MAY;

public class Main {

	// The first argument can be used to select a persistence unit.
	// Check resources/META-INF/persistence.xml for available names.
	public static void main(String[] args) {
		out.println( "== CompletionStage API Example ==" );

		// define some test data
		Author author1 = new Author( "Iain M. Banks" );
		Author author2 = new Author( "Neal Stephenson" );
		Book book1 = new Book( "1-85723-235-6", "Feersum Endjinn", author1, LocalDate.of( 1994, JANUARY, 1 ) );
		Book book2 = new Book( "0-380-97346-4", "Cryptonomicon", author2, LocalDate.of( 1999, MAY, 1 ) );
		Book book3 = new Book( "0-553-08853-X", "Snow Crash", author2, LocalDate.of( 1992, JUNE, 1 ) );
		author1.getBooks().add( book1 );
		author2.getBooks().add( book2 );
		author2.getBooks().add( book3 );

		// obtain a factory for reactive sessions based on the
		// standard JPA configuration properties specified in
		// resources/META-INF/persistence.xml
		try (SessionFactory factory = createEntityManagerFactory( persistenceUnitName( args ) )
				.unwrap( SessionFactory.class )) {

			try (Session session = factory.openSession()) {
				Transaction transaction = session.beginTransaction();
				// persist the Authors with their Books in a transaction
				session.persist( author1 );
				session.persist( author2 );
				transaction.commit();
			}

			try (Session session = factory.openSession()) {
				// retrieve a Book
				Book book = session.find( Book.class, book2.getId() );
				LocalDate published = book.getPublished();
				out.printf( "'%s' was published in %d\n", book.getTitle(), published.getYear() );
			}
		}
	}

	/**
	 * Return the persistence unit name to use in the example.
	 *
	 * @param args the first element is the persistence unit name if present
	 *
	 * @return the selected persistence unit name or the default one
	 */
	public static String persistenceUnitName(String[] args) {
		return args.length > 0 ? args[0] : "postgresql-example";
	}
}
