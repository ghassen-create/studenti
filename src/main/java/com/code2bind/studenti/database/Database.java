package com.code2bind.studenti.database;

import java.sql.*;
import java.util.Dictionary;
import java.util.Enumeration;

public class Database {
    static DatabaseConnection connection = new DatabaseConnection();
    static Connection c;

    static String query = "create database if not exists studenti";

    static {
        try {
            c = connection.connect();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Database() throws SQLException {
        c.close();
        c = connection.connect();
    }

    public ResultSet createTable(String tableName, Dictionary<String, String> fields) throws SQLException {
        ResultSet set;
        query = "create table if not exists " + tableName + " (" +
                "id bigint not null auto_increment,";
        for (Enumeration<String> i = fields.keys(); i.hasMoreElements();)
        {
            String el = i.nextElement();
            query = query.concat(el + " " + fields.get(el) + ",");
        }
        query = query.concat("constraint " + tableName + "_pk primary key (id))");
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        set = statement.getResultSet();
        return set;
    }

    public void alterTable(String name, String field, int size) throws SQLException {
        query = "alter table " + name + " add " + field + " varchar(" + size + ")";
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
    }

    public void alterTable(String name, String field) throws SQLException {
        query = "alter table " + name + " add " + field + " int";
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
    }

    public void alterTable(String name, String field, boolean bool) throws SQLException {
        if (bool){
            query = "alter table " + name + " add " + field + " tinyint";
            PreparedStatement statement = c.prepareStatement(query);
            statement.execute(query);
        }
    }

    public void alterTable(String name, String field, String key, String reference) throws SQLException {
        query = "alter table " + name + " add constraint " + name + "_" + reference + "_fk " +
                "foreign key (" + field + ") references " + reference + " (" + key + ")";
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
    }

    public void InsertData(String name, Dictionary<String, String> values) throws SQLException {
        query = "insert into " + name + "(";
        for (Enumeration<String> i = values.keys(); i.hasMoreElements();)
        {
            String el = i.nextElement();
            if (i.hasMoreElements())
                query = query.concat(el + ", ");
            else
                query = query.concat(el);
        }
        query = query.concat(") values (");
        for (Enumeration<String> i = values.keys(); i.hasMoreElements();) {
            String el = i.nextElement();
            if (i.hasMoreElements())
                query = query.concat("'" + values.get(el) + "', ");
            else
                query = query.concat("'" + values.get(el) + "'");
        }
        query = query.concat(")");
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
    }

    public ResultSet SelectData(String name) throws SQLException {
        ResultSet set;
        query = "select * from " + name;
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        set = statement.getResultSet();
        return set;
    }

    public ResultSet SelectData(String name, String value) throws SQLException {
        ResultSet set;
        query = "select " + value + " from " + name;
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        set = statement.getResultSet();
        return set;
    }

    public ResultSet SelectData(String name, String value, String condition) throws SQLException {
        ResultSet set;
        query = "select " + value + " from " + name + " where " + condition;
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        set = statement.getResultSet();
        return set;
    }

    public ResultSet SelectData(String name, String value, String extra, String condition) throws SQLException {
        ResultSet set;
        query = "select " + value + " from " + name + " " + extra + " where " + condition;
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        set = statement.getResultSet();
        return set;
    }

    @Override
    public String toString() {
        return "database connected.";
    }

    public String[] getFields(String tableName) throws SQLException {
        ResultSet set = this.SelectData(tableName);
        ResultSetMetaData setMetaData = set.getMetaData();
        int count = setMetaData.getColumnCount();
        String[] arr = new String[count];
        for(int i = 1; i<=count; i++) {
            arr[i] = setMetaData.getColumnName(i);
        }
        set.close();
        return arr;
    }

    public ResultSet updateData(String tableName, String field, String value, String condition) throws SQLException {
        query = "update " + tableName + " set " + field + "=" + value + " where " + condition;
        PreparedStatement statement = c.prepareStatement(query);
        statement.execute(query);
        return statement.getResultSet();
    }

    public void close() throws SQLException {
        c.close();
        query = "";
    }
}
