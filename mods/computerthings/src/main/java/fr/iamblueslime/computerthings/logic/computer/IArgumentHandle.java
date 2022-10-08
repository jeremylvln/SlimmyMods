package fr.iamblueslime.computerthings.logic.computer;

public interface IArgumentHandle {
    int count();

    Object[] getAll();
    Object get(int index);
    double getDouble(int index) throws Exception;
    int getInt(int index) throws Exception;
    long getLong(int index) throws Exception;
    boolean getBoolean(int index) throws Exception;
    String getString(int index) throws Exception;
}
