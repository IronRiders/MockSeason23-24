package org.ironriders.lib.sendable_choosers;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.util.sendable.SendableBuilder;
import edu.wpi.first.util.sendable.SendableRegistry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static edu.wpi.first.util.ErrorMessages.requireNonNullParam;

public class CustomSendableChooser<V> implements Sendable, AutoCloseable {
    /**
     * The key for the default value.
     */
    private static final String DEFAULT = "default";

    /**
     * The key for the selected option.
     */
    private static final String SELECTED = "selected";

    /**
     * The key for the active option.
     */
    private static final String ACTIVE = "active";

    /**
     * The key for the option array.
     */
    private static final String OPTIONS = "options";

    /**
     * The key for the instance number.
     */
    private static final String INSTANCE = ".instance";
    private static final AtomicInteger s_instances = new AtomicInteger();
    /**
     * A map linking strings to the objects they represent.
     */
    private final Map<String, V> map = new LinkedHashMap<>();
    private final int instance;
    private final ReentrantLock m_mutex = new ReentrantLock();
    private String defaultChoice = "";
    private String previousVal;
    private Consumer<V> listener;
    private String m_selected;

    @SuppressWarnings("this-escape")
    public CustomSendableChooser() {
        instance = s_instances.getAndIncrement();
        SendableRegistry.add(this, "SendableChooser", instance);
    }

    @Override
    public void close() {
        SendableRegistry.remove(this);
    }

    public void addOption(String name, V object) {
        map.put(name, object);
    }

    public void removeOption(String name) {
        map.remove(name);
    }

    public void clear() {
        map.clear();
    }

    public void setDefaultOption(String name, V object) {
        requireNonNullParam(name, "name", "setDefaultOption");

        defaultChoice = name;
        addOption(name, object);
    }

    public V getSelected() {
        m_mutex.lock();
        try {
            if (m_selected != null) {
                return map.get(m_selected);
            } else {
                return null;
            }
        } finally {
            m_mutex.unlock();
        }
    }

    public void onChange(Consumer<V> listener) {
        requireNonNullParam(listener, "listener", "onChange");
        m_mutex.lock();
        this.listener = listener;
        m_mutex.unlock();
    }

    @Override
    public void initSendable(SendableBuilder builder) {
        builder.setSmartDashboardType("String Chooser");
        builder.publishConstInteger(INSTANCE, instance);
        builder.addStringProperty(DEFAULT, () -> defaultChoice, null);
        builder.addStringArrayProperty(OPTIONS, () -> map.keySet().toArray(new String[0]), null);
        builder.addStringProperty(
                ACTIVE,
                () -> {
                    m_mutex.lock();
                    try {
                        if (m_selected != null) {
                            return m_selected;
                        } else {
                            return defaultChoice;
                        }
                    } finally {
                        m_mutex.unlock();
                    }
                },
                null);
        builder.addStringProperty(
                SELECTED,
                null,
                val -> {
                    V choice;
                    Consumer<V> listener;
                    m_mutex.lock();
                    try {
                        m_selected = val;
                        if (!m_selected.equals(previousVal) && this.listener != null) {
                            choice = map.get(val);
                            listener = this.listener;
                        } else {
                            choice = null;
                            listener = null;
                        }
                        previousVal = val;
                    } finally {
                        m_mutex.unlock();
                    }
                    if (listener != null) {
                        listener.accept(choice);
                    }
                });
    }
}
