package org.nuxeo.ide.studio.editors;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.TitleEvent;
import org.eclipse.swt.browser.TitleListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.nuxeo.ide.studio.StudioPlugin;
import org.nuxeo.ide.studio.connector.internal.StudioConnector;
import org.nuxeo.ide.studio.data.Node;
import org.nuxeo.ide.studio.internal.jdt.ClasspathContainerUpdater;
import org.nuxeo.ide.studio.internal.preferences.PreferencesStore;
import org.nuxeo.ide.studio.views.StudioBrowserView;


public class StudioEditor extends EditorPart {

    public static final String ID = "org.nuxeo.ide.studio.editor";

    protected Browser browser;

    public StudioEditor() {
    }

    @Override
    public void doSave(IProgressMonitor monitor) {

    }

    @Override
    public void doSaveAs() {

    }

    @Override
    public void init(IEditorSite site, IEditorInput input)
            throws PartInitException {
        setSite(site);
        setInput(input);
    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        browser = new Browser(parent, SWT.NONE);
        Node node = (Node) getEditorInput().getAdapter(Node.class);
        String url = PreferencesStore.INSTANCE.getConnectLocation()+"/ide/dev" + node.getKey();
        StudioPlugin.logInfo("-> " + url);
        browser.setUrl(url);
        browser.addTitleListener(new TitleListener() {
            public void changed(TitleEvent event) {
                if (event.title.endsWith("Save Done!")) {
                    StudioPlugin.logInfo("<- save done");
                } else if (event.title.endsWith("Create Done!")) {
                    StudioPlugin.logInfo("<- create done");
                    IWorkbenchWindow window=PlatformUI.getWorkbench().getActiveWorkbenchWindow();
                    IWorkbenchPage page = window.getActivePage();
                    IViewPart view = page.findView(StudioBrowserView.ID);
                    if (view != null) {
                        ((StudioBrowserView)view).refresh();
                    }
                    //TODO not working
                    setPartName("TODO - find id");
                }
            }
        });
    }

    @Override
    public void setFocus() {

    }

    @Override
    public String getPartName() {
        return getEditorInput().getName();
    }

    public Browser getBrowser() {
        return browser;
    }

}
