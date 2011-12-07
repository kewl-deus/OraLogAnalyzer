package oraloganalyzer.util;
/*
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.internal.editors.text.NonExistingFileEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
*/
public class FileUtil
{
/*
	private AbstractUIPlugin plugin;

	public FileUtil(AbstractUIPlugin plugin)
	{
		this.plugin = plugin;
	}

	public String readFile(String filename)
	{
		BufferedReader reader = null;
		try
		{
			char[] buf = new char[4096];
			StringBuffer contents = new StringBuffer(4096);
			reader = new BufferedReader(new FileReader(filename));
			for (int count = reader.read(buf); count != -1; count = reader
					.read(buf))
			{
				contents.append(buf, 0, count);
			}
			return contents.toString();
		} catch (IOException e)
		{
			throw new RuntimeException("Could not read file: " + filename, e);
		} finally
		{
			if (reader != null)
			{
				try
				{
					reader.close();
				} catch (IOException e1)
				{
				}
			}
		}
	}

	public void writeFile(String filename, String contents)
	{
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write(contents);
		} catch (IOException e)
		{
			throw new RuntimeException("Could not write file: " + filename, e);
		} finally
		{
			if (writer != null)
			{
				try
				{
					writer.close();
				} catch (IOException e1)
				{
				}
			}
		}
	}

	public String getPluginRelativePath(Plugin plugin, String relativePath)
	{
		String result = relativePath;
		try
		{
			File defaultLibPath = new File("/");
			URL defaultLib = FileLocator.find(plugin.getBundle(), new Path(
					"plugin.xml"), null);
			URL resolve = FileLocator.resolve(defaultLib);
			defaultLibPath = new File(resolve.getPath()).getCanonicalFile()
					.getParentFile();
			File relPath = new File(defaultLibPath, relativePath);
			result = relPath.getPath();
		} catch (IOException ex)
		{
		}
		return result;
	}

	public IEditorPart openEclipseEditorForFile(String filename)
	{
		return openEclipseEditorForFile(filename, false);
	}

	public IEditorPart openEclipseEditorForFile(String filename, boolean create)
	{
		IEditorPart editorPart = null;
		File file = new File(filename);
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IEditorInput input = createEditorInput(file, create);
		if (input != null)
		{
			String editorId = getEditorId(file);
			IWorkbenchPage page = window.getActivePage();
			try
			{
				editorPart = page.openEditor(input, editorId);
				window.getShell().forceActive();
			} catch (PartInitException e)
			{
				System.err.println("Could not open eclipse editor for file: "
						+ filename);
			}
		}
		if (editorPart == null)
		{
			MessageDialog
					.openError(
							window.getShell(),
							"Could not find file",
							"Could not open the file: "
									+ filename
									+ "\nYou need to supply a filesystem absolute path or a workspace relative one"
									+ "\ne.g." + "\n/tmp/foo.txt"
									+ "\nmyEclipseProject/src/com/foo/Bar.java");
		}
		return editorPart;
	}

	public IEditorPart openEclipseEditorForString(String contents)
	{
		return openEclipseEditorForString(contents,
				EditorsUI.DEFAULT_TEXT_EDITOR_ID);
	}

	public IEditorPart openEclipseEditorForString(String contents,
			String editorId)
	{
		IEditorPart editorPart = null;
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		IPath stateLocation = this.plugin.getStateLocation();
		IPath path = stateLocation.append("/_" + new Object().hashCode());
		IFileStore fileStore = EFS.getLocalFileSystem().getStore(path);
		IEditorInput input = new NonExistingFileEditorInput(fileStore,
				"Untitled Sunshade File");
		IWorkbenchPage page = window.getActivePage();
		try
		{
			editorPart = page.openEditor(input, editorId);

			// Mark the document not-dirty so that it can be closed without a
			// prompt
			ITextFileBufferManager manager = FileBuffers
					.getTextFileBufferManager();
			manager.connect(path, null);
			ITextFileBuffer fileBuffer = manager.getTextFileBuffer(path);
			fileBuffer.getDocument().set(contents);
			fileBuffer.setDirty(false);

			window.getShell().forceActive();
		} catch (Exception e)
		{
			System.err
					.println("Could not open untitled eclipse editor for data");
		}

		if (editorPart == null)
		{
			MessageDialog.openError(window.getShell(), "Could not open editor",
					"Could not open an untitled editor for the given data");
		}
		return editorPart;
	}

	public void selectEclipseEditorRegion(IEditorPart editorPart, int line,
			int colStart, int colEnd)
	{
		if (line >= 0 && editorPart instanceof ITextEditor)
		{
			try
			{
				ITextEditor textEditor = (ITextEditor) editorPart;
				IEditorInput input = editorPart.getEditorInput();
				IDocumentProvider provider = textEditor.getDocumentProvider();
				provider.connect(input);
				IDocument document = provider.getDocument(input);
				int maxLines = document.getNumberOfLines();
				int lineNum = line == 0 ? 1 : line;
				lineNum = Math.min(lineNum, maxLines);
				int fileOffset = document.getLineOffset(lineNum - 1);
				int fileLength = document.getLineLength(lineNum - 1);
				provider.disconnect(input);
				if (fileOffset >= 0 && fileLength >= 0)
				{
					if (colStart >= 0)
					{
						int start = -1, end = -1;
						start = colStart == 0 ? 1 : colStart;
						end = colEnd == 0 ? 1 : colEnd;
						start = Math.min(start, fileLength - 1);
						end = Math.min(end, fileLength - 1);

						fileOffset += start - 1;
						if (colEnd >= 0 && colEnd > colStart)
						{
							fileLength = end - start;
						} else
						{
							fileLength = 0;
						}
					}
					textEditor.selectAndReveal(fileOffset, fileLength);
				}
			} catch (Exception e)
			{
				System.err.println("Unable to select line/column in editor: "
						+ editorPart.getTitle());
			}
		}
	}

	private String getEditorId(File file)
	{
		String editorId = EditorsUI.DEFAULT_TEXT_EDITOR_ID;
		IWorkbench workbench = PlatformUI.getWorkbench();
		IEditorRegistry editorRegistry = workbench.getEditorRegistry();
		IEditorDescriptor descriptor = editorRegistry.getDefaultEditor(file
				.getName());
		if (descriptor != null)
		{
			editorId = descriptor.getId();

			// For some reason in eclipse 3.2.2 (and others?) the editorId
			// returned for an html file causes the page.openEditor call to not
			// return an EditorPart, even though the html file gets successfully
			// opened in the browser editor. This thus causes my error dialog.
			// Force this other editorInput to prevent that for html files
			//
			if (editorId.equals("org.eclipse.ui.browser.editorSupport"))
			{
				editorId = "org.eclipse.ui.browser.editor";
			}
		}
		return editorId;
	}

	private IEditorInput createEditorInput(File file, boolean create)
	{
		if (file == null)
		{
			return null;
		}
		IEditorInput result = null;
		IFile workspaceFile = getWorkspaceRelativeFile(file);
		// If we havea file in the workspace, return the input for it, else
		// return an input for an External file
		if (workspaceFile != null)
		{
			try
			{
				workspaceFile.refreshLocal(IResource.DEPTH_ONE, null);
			} catch (CoreException e1)
			{
				System.err.println("Problem refreshing workspace resource");
			}
			if (create && !workspaceFile.exists())
			{
				try
				{
					workspaceFile.create(new ByteArrayInputStream(new byte[0]),
							true, null);
				} catch (Exception e)
				{
					System.err.println("Unable to create new workspace file");
				}
			}
			if (workspaceFile.exists())
			{
				result = new FileEditorInput(workspaceFile);
			}
		} else
		{
			if (create && !file.exists())
			{
				try
				{
					file.createNewFile();
				} catch (Exception e)
				{
					System.err.println("Unable to create new external file");
				}
			}
			if (file.exists())
			{
				// IFileStore fileStore = EFS.getLocalFileSystem().getStore(new
				// Path(file.getPath()));
				// result = new FileStoreEditorInput(fileStore);
				result = new PathEditorInput(new Path(file.getPath()));
			}
		}
		return result;
	}

	// If File is a workspace relative file, return eclipse resource for it.
	// By workspace relative I mean "project/path/to/file.txt"
	private IFile getWorkspaceRelativeFile(File file)
	{
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IPath location = null;
		if (file.isAbsolute())
			location = new Path(file.getAbsolutePath());
		else
			location = new Path(workspace.getRoot().getLocation()
					+ File.separator + file.getPath());
		IFile[] files = workspace.getRoot().findFilesForLocation(location);
		if (files == null || files.length == 0)
			return null;
		if (files.length == 1)
			return files[0];
		return selectWorkspaceFile(files);
	}

	private IFile selectWorkspaceFile(IFile[] files)
	{
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				new FileLabelProvider());
		dialog.setElements(files);
		dialog.setTitle("Select Workspace File");
		dialog
				.setMessage("The selected file is referenced by multiple linked resources in the workspace.\nPlease select the workspace resource you want to use to open the file.");
		if (dialog.open() == Window.OK)
			return (IFile) dialog.getFirstResult();
		return null;
	}

	// The code below came mostly from the eclipse class
	// OpenExternalEditorAction

	private class FileLabelProvider extends LabelProvider
	{
		public String getText(Object element)
		{
			if (element instanceof IFile)
			{
				IPath path = ((IFile) element).getFullPath();
				return path != null ? path.toString() : ""; //$NON-NLS-1$
			}
			return super.getText(element);
		}
	}
*/
}
