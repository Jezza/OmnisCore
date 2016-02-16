package me.jezza.oc.common.core.command;

import static me.jezza.oc.common.utils.helpers.StringHelper.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.google.common.base.Splitter;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import me.jezza.oc.OmnisCore;
import me.jezza.oc.common.interfaces.Command;
import me.jezza.oc.common.utils.Debug;
import me.jezza.oc.common.utils.collect.BranchedMap;
import me.jezza.oc.common.utils.collect.Collections3;
import net.minecraft.command.ICommandSender;

/**
 * @author Jezza
 */
public class CommandInstance {
	private static final Object[] EXPECTED_PARAMETER_TYPES = new Object[]{ICommandSender.class, String[].class};
	private static final String EXPECTED_PARAMETER_TYPES_STRING = Arrays.toString(EXPECTED_PARAMETER_TYPES);

	private final Map<String, CommandDelegate> delegateMap;

	public CommandInstance(Class<? extends CommandAbstract> commandClass, Map<String, Object> annotationInfo) {
		Command classCommand = commandClass.getAnnotation(Command.class);
		if (classCommand == null)
			throw new IllegalStateException(format("@{} was not found on the class: {}.", Command.class.getSimpleName(), commandClass.getCanonicalName()));
		Iterable<String> commandNames = Splitter.on(classCommand.nameSplitter()).trimResults().omitEmptyStrings().split(classCommand.value());
		String commandName = null;
		Set<String> delegates = new HashSet<>();
		List<String> aliases = new ArrayList<>();
		for (String value : commandNames) {
			String name = split(value)[0];
			if (commandName == null) {
				commandName = name;
			} else {
				aliases.add(name);
			}
			delegates.add(name);
		}
		if (commandName == null)
			throw new IllegalStateException(format("No valid command name was provided for {}.", commandClass.getCanonicalName()));
		int activeSide = CommandRegistry.side();
		int classSide = classCommand.side();
		BranchedMap<String, Method> methodMap = new BranchedMap<>(null, true, true);
		for (Method method : commandClass.getDeclaredMethods()) {
			Command methodCommand = method.getAnnotation(Command.class);
			if (methodCommand == null)
				continue;
			Class<?>[] parameterTypes = method.getParameterTypes();
			if (parameterTypes.length != 2)
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid parameter length. Method: {}, Expected parameters: {}, discovered: {}.", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			if (!arrayContentsEqual(EXPECTED_PARAMETER_TYPES, parameterTypes))
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid parameter types. Method: {}, Expected parameters: {}, discovered: {}.", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			String value = methodCommand.value();
			if (!useable(value))
				throw new IllegalArgumentException(format("Method has been annotated with @{}, but it has an invalid command value. (This could mean it's either empty, or all whitespace.) Method: {}", Command.class.getSimpleName(), EXPECTED_PARAMETER_TYPES_STRING, Arrays.toString(parameterTypes)));
			int methodSide = methodCommand.side();
			if ((classSide & methodSide) == 0)
				throw new IllegalArgumentException(format("Method has been given a specific side to be registered on that the command never will be. This is not allowed. Class side: {}, Method Side: {}, Method: {}. ", CommandRegistry.name(classSide), CommandRegistry.name(methodSide), method));
			if ((activeSide & methodSide) == 0)
				continue;
			final String joiner;
			if (useable(methodCommand.joiner())) {
				joiner = methodCommand.joiner();
			} else if (useable(classCommand.joiner())) {
				joiner = classCommand.joiner();
			} else {
				joiner = Command.DEFAULT_JOINER;
			}
			Iterable<String> sub = Splitter.on(methodCommand.nameSplitter()).trimResults().omitEmptyStrings().split(value);
			for (String name : commandNames) {
				for (String subCommand : sub) {
					String[] split = split(name + joiner + subCommand);
					Method oldMethod = methodMap.put(split, method);
					if (oldMethod != null)
						throw new IllegalStateException(format("Multiple methods exist with the same command signature. {}.{} and {}.{}", commandClass.getCanonicalName(), method.getName(), commandClass.getCanonicalName(), oldMethod.getName()));
					String delegateName = split[0];
					if (!commandName.equals(delegateName) && !aliases.contains(delegateName) && !delegates.contains(delegateName))
						delegates.add(name);
				}
			}
		}

		if (Debug.console())
			OmnisCore.logger.info("Active MethodMap for {}:\n{}", commandClass.getCanonicalName(), methodMap);
		final CommandAbstract instance;
		try {
			Constructor<? extends CommandAbstract> constructor = commandClass.getDeclaredConstructor(String.class, List.class, BranchedMap.class);
			instance = constructor.newInstance(commandName, Collections3.immutable(aliases), methodMap);
		} catch (NoSuchMethodException e) {
			OmnisCore.logger.error(format("No valid constructor found. Expected: [{}, {}, {}]", String.class.getCanonicalName(), List.class.getCanonicalName(), BranchedMap.class), e);
			throw Throwables.propagate(e);
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			OmnisCore.logger.error(format("Failed to create command instance from {}.", commandClass.getCanonicalName()), e);
			throw Throwables.propagate(e);
		}

		Builder<String, CommandDelegate> builder = ImmutableMap.builder();
		for (String delegate : delegates) {
			builder.put(delegate, new CommandDelegate(delegate, instance));
		}
		delegateMap = builder.build();
	}

	public Map<String, CommandDelegate> delegateMap() {
		return delegateMap;
	}

	private static boolean arrayContentsEqual(Object[] a1, Object[] a2) {
		if (a1 == null)
			return a2 == null || a2.length == 0;
		if (a2 == null)
			return a1.length == 0;
		if (a1.length != a2.length)
			return false;
		for (int i = 0; i < a1.length; i++)
			if (a1[i] != a2[i])
				return false;
		return true;
	}
}
