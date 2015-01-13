package org.nuxeo.ide.qatests.dialogs;

import java.util.HashMap;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.nuxeo.ide.qatests.Bot;
import org.nuxeo.ide.qatests.wizards.SDKPreferenceBot;

public class NuxeoPreferencesBot extends DialogBot {

    protected SWTBotTreeItem nuxeo;

    protected SWTBotButton ok;

    public NuxeoPreferencesBot(SWTWorkbenchBot workbench) {
        super(workbench, "Preferences");
    }

    @Override
    protected void handleActivation() {
        SWTBotTree tree = workbench.tree();
        tree.expandNode("Nuxeo");
        nuxeo = tree.getTreeItem("Nuxeo");
        ok = workbench.button("OK");
    }

    public enum Choice {

        SDK(SDKPreferenceBot.class, "Nuxeo SDK");

        protected final Class<? extends Bot> botClass;

        protected final String name;

        Choice(Class<? extends Bot> botClass, String name) {
            this.botClass = botClass;
            this.name = name;
        }
    }

    protected static HashMap<Class<? extends Bot>, Choice> choices =
            new HashMap<Class<? extends Bot>, Choice>();

    static {
        for (Choice p:Choice.values()) {
            choices.put(p.botClass, p);
        }
    }

    protected <T extends Bot> T newBot(Class<T> botClass) {
        try {
            return botClass.getConstructor(new Class<?>[] { SWTWorkbenchBot.class }).newInstance(workbench);
        } catch (Exception e) {
           throw new Error("Cannot instanciate bot of class " + botClass.getName(), e);
        }
    }

    public <T extends Bot> T  select(Class<T> botClass) {
        Choice choice = choices.get(botClass);
        nuxeo.select(choice.name);
        return newBot(botClass);
    }

    public void finish() {
        ok.click();
    }
}
