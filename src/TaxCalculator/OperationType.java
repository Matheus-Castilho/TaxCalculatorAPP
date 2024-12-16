package TaxCalculator;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author matheuscastilho
 */
public enum OperationType {
    LOGIN,
    UPDATE_PROFILE,
    DELETE_USER,
    REGISTER,
    TAX_CALCULATION;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
