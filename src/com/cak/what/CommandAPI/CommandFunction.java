package com.cak.what.CommandAPI;

import com.cak.what.Annotations.ApiInternal;
import com.cak.what.Util.ChCol;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * <h2>Interface for command listeners</h2>
 *
 * Methods / Lambdas will be passed all the arguments from the command:
 * <ul>
 *     <li>{@code CommandSender sender} - The sender of the command</li>
 *     <li>{@code Command command} - The command that was executed</li>
 *     <li>{@code String label} - The command label</li>
 *     <li>{@code String[] args} - The arguments passed to the command</li>
 * </ul>
 *
 * <hr><br>
 *
 * Example usages:
 * <blockquote>
 * You can either use a lambda or a method reference,<br><br>
 *
 * Lambda Example:<br>
 *
 * <code><pre>
 * //Argument names can be changed, however,
 * //The order of the arguments cannot.
 * CommandFunction cmdFunc = (sender, command, label, args) -> {
 *    sender.sendMessage("Hello World!");
 *    //Return true to indicate that the command
 *    //was handled successfully
 *    return true;
 * };</pre></code>
 *
 * Method Reference Example:<br>
 *
 * <code><pre>
 * //Will pass exampleCommandHandler() in
 * //YourHandlerClass as the CommandFunction
 * CommandFunction cmdFunc2 =
 *    YourCommandHandlerClass::exampleCommandHandler
 * ;
 * </pre></code>
 *
 * </blockquote>
 *
 * @see CommandHandler
 * @see CommandFunction#onCommand(CommandSender sender, Command command, String label, String[] args)
 */
public interface CommandFunction {
    /**
     * <h2>Command Function - Used to handle commands registered via <a href="CommandHandler">CommandHandler</a></h2>
     * Methods / Lambdas will be passed all the arguments from the command:
     * <ul>
     *     <li>{@code CommandSender sender} - The sender of the command</li>
     *     <li>{@code Command command} - The command that was executed</li>
     *     <li>{@code String label} - The command label</li>
     *     <li>{@code String[] args} - The arguments passed to the command</li>
     * </ul>
     * <hr><br>
     *
     * Example usages:
     * <blockquote>
     * You can either use a lambda or a method reference,<br><br>
     *
     * Lambda Example:<br>
     *
     * <code><pre>
     * //Argument names can be changed, however,
     * //The order of the arguments cannot.
     * CommandFunction cmdFunc = (sender, command, label, args) -> {
     *    sender.sendMessage("Hello World!");
     *    //Return true to indicate that the command
     *    //was handled successfully
     *    return true;
     * };</pre></code>
     *
     * Method Reference Example:<br>
     *
     * <code><pre>
     * //Will pass exampleCommandHandler() in
     * //YourHandlerClass as the CommandFunction
     * CommandFunction cmdFunc2 =
     *    YourCommandHandlerClass::exampleCommandHandler
     * ;
     * </pre></code>
     *
     * </blockquote>
     *
     * @param sender The sender of the command
     * @param command The command being executed
     * @param label The label of the command
     * @param args The arguments of the command
     * @return True if the command was handled, false if not
     */
    boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    HelpMessage helpMessage = new HelpMessage();

    @ApiInternal
    default boolean onCommandWrapper(CommandSender sender, Command command, String label, String[] args) {
        try {
            if (Objects.equals(args[0], "help")) {
                sendHelp(sender);
                return true;
            } else {
                return onCommand(sender, command, label, args);
            }
        } catch (IndexOutOfBoundsException ignored) {}
        return onCommand(sender, command, label, args);

    }

    @ApiInternal
    default void sendHelp(CommandSender sender) {
        if (!helpMessage.isEmpty()) {
            helpMessage.send(sender);
        } else {
            sender.sendMessage( ChCol.GOLD + "No help message available for '" + helpMessage.getCmdName() + "'!");
        }
    }

    @ApiInternal
    default void setCmdName(String cmdName) {
        helpMessage.setCmdName(cmdName);
    }

    @ApiInternal
    default void setHelpMessage(String cmdName, HelpArgument... args) {
        this.helpMessage.set(cmdName, args);
    }

}
