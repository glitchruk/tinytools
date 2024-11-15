package com.github.glitchruk.tinytools.concurrent;

/**
 * A thread-safe utility class for lazy initialization.
 * <p>
 * The {@code Lazy} class provides a mechanism for deferred initialization
 * of an object. The value is set only once and can then be accessed multiple times.
 * This is particularly useful in scenarios where an expensive computation or
 * initialization should be postponed until the first access, while maintaining
 * thread safety and ensuring efficient subsequent reads.
 * </p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *     <li>Single initialization with {@code set(T value)}.</li>
 *     <li>Thread-safe access to the value using {@code get()}.</li>
 *     <li>Initialization state checking with {@code isInitialized()}.</li>
 * </ul>
 *
 * <p>
 * This class is designed for cases where a value is assigned exactly once and is expected
 * to be accessed multiple times. All methods are {@code synchronized}, ensuring
 * safe concurrent usage.
 * </p>
 *
 * <p><strong>Thread Safety:</strong> If the value type {@code T} is mutable, users must take care
 * to avoid modifying the value after it has been initialized, as such modifications are
 * not thread-safe. For simpler and safer usage, immutable types are recommended.
 * </p>
 *
 * <p><strong>Note:</strong> In the example below, the {@code Integer} type is used as
 * a wrapper for the primitive {@code int}. This is required because generics in Java
 * work only with reference types, not primitives. For most use cases, this distinction
 * is irrelevant, but it is worth noting when working with primitive values.
 * </p>
 *
 * <p><strong>Example Usage:</strong></p>
 * <pre>{@code
 * public final class Person {
 *     private final Lazy<Integer> age = new Lazy<>();
 *
 *     public Person() {
 *         // Constructor does not set age; it will be initialized later
 *     }
 *
 *     public void initializeAge(final int initialAge) {
 *         // Initialize the age only once
 *         age.set(initialAge);
 *     }
 *
 *     public int getAge() {
 *         return age.get();
 *     }
 * }
 *
 * public class Main {
 *     public static void main(String[] args) {
 *         Person person = new Person();
 *         person.initializeAge(42); // Deferred initialization
 *         System.out.println("Age: " + person.getAge()); // Outputs: Age: 42
 *     }
 * }
 * }</pre>
 *
 * @param <T> the type of the value to be lazily initialized
 */
public class Lazy<T> {
    private T value;
    private boolean initialized;

    /**
     * Creates a new {@code Lazy} instance with no initial value.
     */
    public Lazy() {
        this.value = null;
        this.initialized = false;
    }

    /**
     * Initializes the value if it has not already been set.
     *
     * @param value the value to set
     * @throws IllegalStateException if the value has already been initialized
     */
    public synchronized void set(final T value) {
        if (initialized) {
            throw new IllegalStateException("Lazy value already initialized");
        }
        this.value = value;
        this.initialized = true;
    }

    /**
     * Retrieves the initialized value.
     *
     * @return the initialized value
     * @throws IllegalStateException if the value has not been initialized
     */
    public synchronized T get() {
        if (!initialized) {
            throw new IllegalStateException("Lazy value not initialized");
        }
        return value;
    }

    /**
     * Checks if the value of the memoized object has been set.
     *
     * @return {@code true} if the value has been set, {@code false} otherwise
     */
    public synchronized boolean isInitialized() {
        return initialized;
    }
}