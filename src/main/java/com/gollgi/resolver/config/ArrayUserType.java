package com.gollgi.resolver.config;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.context.annotation.Configuration;

/**
 * This custom class defining a postgreSQL array that can be persist by JPA.
 * For use in entity define @type(type = "com.crawler.app.ArrayUserType")
 * annotation above String[].
 * (An array support for hibernate-postgreSQL implementation )
 * @author Hoffman
 */
@Configuration
public class ArrayUserType implements UserType {

	/**
	 * Constante contenant le type SQL "Array".
	 */
	protected static final int[] SQL_TYPES = { Types.ARRAY };

	/**
	 * Return the SQL type codes for the columns mapped by this type. The codes are
	 * defined on <tt>java.sql.Types</tt>.
	 * 
	 * @return int[] the typecodes
	 * @see java.sql.Types
	 */
	public final int[] sqlTypes() {
		return SQL_TYPES;
	}

	/**
	 * The class returned by <tt>nullSafeGet()</tt>.
	 * 
	 * @return Class
	 */
	public final Class returnedClass() {
		return String[].class;
	}

	/**
	 * Retrieve an instance of the mapped class from a JDBC resultset. Implementors
	 * should handle possibility of null values.
	 * 
	 * @param resultSet
	 *            a JDBC result set.
	 * @param names
	 *            the column names.
	 * @param session
	 *            SQL en cours.
	 * @param owner
	 *            the containing entity
	 * @return Object
	 * @throws org.hibernate.HibernateException
	 *             exception levée par Hibernate lors de la récupération des
	 *             données.
	 * @throws java.sql.SQLException
	 *             exception SQL levées lors de la récupération des données.
	 */
	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
			throws HibernateException, SQLException {
		if (rs.wasNull()) {
			return null;
		}

		String[] array = (String[]) rs.getArray(names[0]).getArray();
		return array;
	}

	
	/**
	 * Write an instance of the mapped class to a prepared statement. Implementors
	 * should handle possibility of null values. A multi-column type should be
	 * written to parameters starting from <tt>index</tt>.
	 * 
	 * @param statement
	 *            a JDBC prepared statement.
	 * @param value
	 *            the object to write
	 * @param index
	 *            statement parameter index
	 * @param session
	 *            sql en cours
	 * @throws org.hibernate.HibernateException
	 *             exception levée par Hibernate lors de la récupération des
	 *             données.
	 * @throws java.sql.SQLException
	 *             exception SQL levées lors de la récupération des données.
	 */
	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
			throws HibernateException, SQLException {

		if (value == null) {
			st.setNull(index, SQL_TYPES[0]);
		} else {
			String[] castObject = (String[]) value;
			Array array = session.connection().createArrayOf("text", castObject);
			st.setArray(index, array);
		}
	}





	
	@Override
	public final Object deepCopy(final Object value) throws HibernateException {
		return value;
	}

	@Override
	public final boolean isMutable() {
		return false;
	}

	@Override
	public final Object assemble(final Serializable arg0, final Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final Serializable disassemble(final Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final boolean equals(final Object x, final Object y) throws HibernateException {
		if (x == y) {
			return true;
		} else if (x == null || y == null) {
			return false;
		} else {
			return x.equals(y);
		}
	}

	@Override
	public final int hashCode(final Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public final Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
		return original;
	}

}