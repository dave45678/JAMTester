/*
 * Decompiled with CFR 0_102.
 */
package org.jext.console.commands;

import org.jext.console.Console;

public abstract class Command {
    public Command next;

    public abstract String getCommandName();

    public abstract String getCommandSummary();

    public abstract boolean handleCommand(Console var1, String var2);
}

