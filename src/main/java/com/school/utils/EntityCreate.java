package com.school.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang.exception.ExceptionUtils;

public class EntityCreate {

	@SuppressWarnings("unused")
	private String dependencyString = "<dependency> <groupId>org.postgresql</groupId>  <artifactId>postgresql</artifactId> <version>42.2.11</version></dependency>";

	public static void main(String[] args) {

		String tableName = "students";
		String tableSeq = "students_seq";
		
		System.out.println("@Entity\r\n"
				+ "@Table\r\n"
				+ "@DynamicInsert\r\n"
				+ "@DynamicUpdate\r\n"
				+ "@SequenceGenerator(allocationSize = 1,name = \""+tableSeq+"\",sequenceName = \""+tableSeq+"\")\r\n"
				+ "public class Students {");
		
		System.out.println();
		
		String tableQry = "select * from information_schema.columns where table_name = '"+tableName+"'";
		getColumnsData(tableQry);
		System.out.println(" }");
		
	}

	/**
	 * 
	 * @author Vignesh
	 * @createdOn Aug 29, 2020
	 */
	private static void getColumnsData(String tableQry) {

		try (Connection devqaCon = getConnectleader12Connection();
				PreparedStatement ps = devqaCon.prepareStatement(tableQry);
				ResultSet rSet = ps.executeQuery();) {

			while (rSet.next()) {

				String columnName = rSet.getString("column_name");

				String entityColumnName = titleCaseConversion(columnName);

				String dataType = rSet.getString("data_type");

				String columnDefault = rSet.getString("column_default");

				String characterMaximumLength = rSet.getString("character_maximum_length");

				String isNullable = rSet.getString("is_nullable");

				// @Column(name = "share_this_multi_touch_with", columnDefinition = "character
				// varying(255)")

				if ( dataType.startsWith("bigint") ||  dataType.startsWith("int") || dataType.startsWith("serial")) {

					if( "id".equals(columnName) ) {
						System.out.println("@Id");
					}
					
					String entityColumn = "";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					String columnDefinition = "";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", columnDefinition = \"" + columnDefinition + "\"";

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println(entityColumn);

					if(dataType.startsWith("bigint") || "id".equals(columnName)) {
						
						System.out.println("private Long " + entityColumnName + ";");
					} else {
						
						System.out.println("private Integer " + entityColumnName + ";");
					}

				} else if (dataType.startsWith("bool")) {

					String entityColumn = "";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					String columnDefinition = "";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", columnDefinition = \"" + columnDefinition + "\"";

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println(entityColumn);

					System.out.println("private Boolean " + entityColumnName + ";");

				} else if (dataType.startsWith("time")) {

					String entityColumn = "";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					String columnDefinition = "";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", columnDefinition = \"" + columnDefinition + "\"";

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println(entityColumn);

					System.out.println("@JsonFormat(pattern = \"yyyy-MM-dd'T'HH:mm:ss'Z'\") ");

					System.out.println("private LocalDateTime " + entityColumnName + ";");

				} else if (dataType.startsWith("date")) {

					String entityColumn = "";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					String columnDefinition = "";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", columnDefinition = \"" + columnDefinition + "\"";

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println(entityColumn);

					System.out.println("@JsonFormat(pattern = \"yyyy-MM-dd'T'HH:mm:ss'Z'\")");

					System.out.println("private LocalDate " + entityColumnName + ";");

				} else if (dataType.startsWith("json")) {

					String entityColumn = "";

					String columnDefinition = " columnDefinition = \"json\"";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += ", length=" + characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", " + columnDefinition;

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println("@Type(type = \"json\")");

					System.out.println(entityColumn);

					System.out.println("private JsonNode " + entityColumnName + ";");

				} else if (dataType.startsWith("varchar") || dataType.startsWith("text")
						|| dataType.startsWith("character") || dataType.startsWith("bpchar")) {

					String entityColumn = "";

					entityColumn += "@Column(name = \"" + columnName + "\"";

					String columnDefinition = "";

					if ( columnDefault != null && !columnDefault.isBlank())
						columnDefinition += "default " + columnDefault;

					if (characterMaximumLength != null && !characterMaximumLength.isBlank())
						entityColumn += ", length=" + characterMaximumLength;

					if ( columnDefinition != null && !columnDefinition.isBlank())
						entityColumn += ", columnDefinition = \"" + columnDefinition + "\"";

					entityColumn += ")";

					if ("no".equalsIgnoreCase(isNullable))
						System.out.println("@NotNull(message=\"" + entityColumnName + " must not be null\")");

					System.out.println(entityColumn);

					System.out.println("private String " + entityColumnName + ";");

				}

				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unused")
	private static void getEntityData(String string) {

		try (Connection devqaCon = getConnectleader12Connection();
				PreparedStatement ps = devqaCon.prepareStatement(string);
				ResultSet rSet = ps.executeQuery();) {

			ResultSetMetaData metaData = rSet.getMetaData();

			int count = metaData.getColumnCount();

			for (int i = 1; i <= count; i++) {

				String columnName = metaData.getColumnName(i);

				if (metaData.getColumnTypeName(i).startsWith("int")
						|| metaData.getColumnTypeName(i).startsWith("serial")) {

					System.out.println("@Column(name = \"" + columnName + "\")");// @Column(name="metrics_id")

					if (i == 1)
						System.out.println("private Long " + titleCaseConversion(columnName));
					else
						System.out.println("private Integer " + titleCaseConversion(columnName));

				} else if (metaData.getColumnTypeName(i).startsWith("bool")) {

					System.out.println("@Column(name = \"" + columnName + "\")");

					System.out.println("private Boolean " + titleCaseConversion(columnName));

				} else if (metaData.getColumnTypeName(i).startsWith("time")) {

					System.out.println("@JsonFormat(pattern = \"yyyy-MM-dd'T'HH:mm:ss'Z'\")  @Column(name = \""
							+ columnName + "\")");

					System.out.println("private LocalDateTime " + titleCaseConversion(columnName));

				} else if (metaData.getColumnTypeName(i).startsWith("date")) {

					System.out.println("@JsonFormat(pattern = \"yyyy-MM-dd'T'HH:mm:ss'Z'\") @Column(name = \""
							+ columnName + "\")");

					System.out.println("private LocalDateTime " + titleCaseConversion(columnName));

				} else if (metaData.getColumnTypeName(i).startsWith("varchar")
						|| metaData.getColumnTypeName(i).startsWith("text")
						|| metaData.getColumnTypeName(i).startsWith("character")
						|| metaData.getColumnTypeName(i).startsWith("bpchar")) {

					System.out.println("@Column(name = \"" + columnName + "\")");

					System.out.println("private String " + titleCaseConversion(columnName));

				}

				System.out.println();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static String titleCaseConversion(String inputString) {

		if (inputString.isBlank()) {
			return "";
		}

		if (inputString.length() == 1 || inputString.split("_").length == 1) {
			return inputString;
		}

		StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());

		String[] data = inputString.split("_");

		int i = 0;
		for (String stringPart : data) {

			char[] charArray = stringPart.toLowerCase().toCharArray();

			if (i != 0)
				charArray[0] = Character.toUpperCase(charArray[0]);

			resultPlaceHolder.append(new String(charArray)).append("");

			i++;
		}

		return resultPlaceHolder.toString().trim();
	}

	public static Connection getConnectleader12Connection() {

		String localurl = "jdbc:postgresql://localhost/school-db", username = "postgres", password = "admin";

		Connection connection = null;

		try {

			Class.forName("org.postgresql.Driver");

			connection = DriverManager.getConnection(localurl, username, password);

		} catch (Exception e) {

			System.out
					.println("DBConnection.getDevqaConnection() , Exception = " + ExceptionUtils.getFullStackTrace(e));

		}

		return connection;
	}
}
