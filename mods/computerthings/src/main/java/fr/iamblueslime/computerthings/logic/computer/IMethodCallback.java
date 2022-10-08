package fr.iamblueslime.computerthings.logic.computer;

@FunctionalInterface
public interface IMethodCallback {
    Object[] apply(IArgumentHandle args) throws Exception;
}
