package fr.iamblueslime.computerthings.logic.computer;

public interface IComputerPeripheral {
    Object[] invokePeripheralMethod(int index, IArgumentHandle args) throws Exception;
    String[] getPeripheralMethods();
    String getPeripheralName();
}
