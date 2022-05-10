package com.esstu.dymbrylov.services;

import com.esstu.dymbrylov.controllers.ModalController;
import com.esstu.dymbrylov.model.Material;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Map;
import java.util.Objects;

public class MaterialService extends MainService{
    ModalController modalWindow = new ModalController();

    public MaterialService() {
        super();
    }

    public ObservableList<Material> getAllMaterial() {
        ObservableList<Material> materials = FXCollections.observableArrayList();
        ResultSet resultSet = null;
        String query = "SELECT * FROM material";

        try {
            connect = ConnectorDB();
            preparedStatement = connect.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Material material = new Material(id, name);
                materials.add(material);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            modalWindow.showError(e);
        }finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    modalWindow.showError(e);
                }
            }
        }
        return materials;
    }
    public Map.Entry<Boolean, String> setMaterialByName(String textName) {
        Map.Entry<Boolean, String> response = Map.entry(false, "Произошла ошибка при сохранении материала");
        ObservableList<Material> materials = getAllMaterial();

        connect = ConnectorDB();
        Object[] newMaterials = materials.stream().
                filter(mat -> Objects.equals(mat.getName(), textName)).toArray();
        try  {
            if (Objects.equals(textName, "")) {
                response = Map.entry(false, "Заполните поле");

            }else if (newMaterials.length == 0) {
                String query = "INSERT INTO material(name) VALUES(?)";
                preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, textName);
                int result = preparedStatement.executeUpdate();

                if (result == 1) {
                    response = Map.entry(true, "Материал добавлен");
                }
            }else {
                response = Map.entry(true, "Уже есть такой материал");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            modalWindow.showError(e);
        }finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    modalWindow.showError(e);
                }
            }
        }
        return response;
    }

    public Integer getMaterialByName(String name) {
        Integer idMaterial = null;
        connect = ConnectorDB();
        String query = "SELECT * from material where name = ?";
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idMaterial = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            modalWindow.showError(e);
        }finally {
            if (connect != null) {
                try {
                    connect.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    modalWindow.showError(e);
                }
            }
        }
        return idMaterial;
    }
}