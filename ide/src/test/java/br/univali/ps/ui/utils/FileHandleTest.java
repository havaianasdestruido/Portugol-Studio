package br.univali.ps.ui.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileHandleTest
{
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void saveWritesUtf8ContentAndReplacesExistingFile() throws Exception
    {
        File target = temporaryFolder.newFile("programa.por");
        Files.write(target.toPath(), "conteúdo antigo".getBytes(Charset.forName("UTF-8")));

        FileHandle.save("escreva(\"Olá\")", target);

        assertEquals("escreva(\"Olá\")", new String(Files.readAllBytes(target.toPath()), Charset.forName("UTF-8")));
    }

    @Test
    public void saveFailsInsteadOfSilentlyIgnoringMissingParent() throws Exception
    {
        File target = new File(temporaryFolder.getRoot(), "missing/programa.por");
        try
        {
            FileHandle.save("conteúdo", target);
            fail("Saving into a missing directory must fail visibly");
        }
        catch (Exception expected)
        {
            // The editor can now report the failure and offer Save As.
            assertTrue(expected instanceof IOException);
        }
        assertFalse(target.exists());
    }
}
