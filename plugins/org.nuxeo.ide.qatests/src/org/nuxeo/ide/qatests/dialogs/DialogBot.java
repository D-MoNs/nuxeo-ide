package org.nuxeo.ide.qatests.dialogs;

import java.lang.reflect.Constructor;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class DialogBot {

    public final SWTWorkbenchBot workbench;

    public final String title;

    public SWTBotShell shell;

    public DialogBot(SWTWorkbenchBot workbench, String title) {
        this.workbench = workbench;
        this.title = title;
    }

    public boolean isDisplayed() {
        return shell != null;
    }

    protected SWTBotShell waitShellUntil() {
        workbench.waitUntil(new DefaultCondition() {

            public boolean test() throws Exception {
                return workbench.shell(title) != null;
            }

            public String getFailureMessage() {
                return "Cannot find shell " + title;
            }
        });
        shell = workbench.shell(title);
        shell.activate();
        handleActivation();
        return shell;
    }

    protected void handleActivation() {
        ;
    }
 
    protected static <T extends DialogBot> T newDialog(SWTWorkbenchBot workbench, Class<T> dialogClass) {
        try {
        Constructor<T> c = dialogClass.getConstructor(new Class<?>[] { SWTWorkbenchBot.class });
        return c.newInstance(workbench);       
        } catch (Exception e) {
            throw new Error("Cannot create dialog of type " + dialogClass.getName(), e);
        }
    }
    
    public static <T extends DialogBot> T asyncOpen(SWTWorkbenchBot workbench, final Class<T> dialogClass,
            final DialogOperation<T> op)  {
        final T dialog = newDialog(workbench, dialogClass);
        // opens the dialog asynchronous in the UI thread
        UIThreadRunnable.asyncExec(SWTUtils.display(), new VoidResult() {
            public void run() {
                op.run(dialog);
            }
        });
        dialog.waitShellUntil();
        return dialog;
    }

    public static <T extends DialogBot> T asyncExecute(SWTWorkbenchBot workbench, final Class<T> dialogClass,
            final DialogOperation<T> op) {
        final T dialog = newDialog(workbench, dialogClass);
        UIThreadRunnable.asyncExec(SWTUtils.display(), new VoidResult() {
            public void run() {
                if (dialog.shell == null) {
                    dialog.waitShellUntil();
                }
                op.run(dialog);
            }
        });
        return dialog;
    }

}
