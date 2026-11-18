package br.univali.ps.ui.utils;

import br.univali.ps.nucleo.ExcecaoAplicacao;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;

public final class FileHandle
{
    private static final Logger LOGGER = Logger.getLogger(FileHandle.class.getName());
    private static final String charsetPadrao = "UTF-8";

    public static void save(String text, File file) throws Exception{
        save(text, file, charsetPadrao);
    }
    public static void save(String text, File file, String charset) throws Exception
    {
        if (file == null)
        {
            throw new IOException("O arquivo de destino não foi informado.");
        }

        java.nio.file.Path destino = file.toPath().toAbsolutePath();
        java.nio.file.Path pasta = destino.getParent();
        if (pasta == null || !java.nio.file.Files.isDirectory(pasta))
        {
            throw new IOException("A pasta de destino não existe: " + pasta);
        }
        if (java.nio.file.Files.exists(destino) && !java.nio.file.Files.isWritable(destino))
        {
            throw new IOException("Sem permissão para gravar no arquivo: " + destino);
        }

        java.nio.file.Path temporario = null;
        try
        {
            // Grave primeiro em um arquivo temporário na mesma pasta para não truncar o original
            // se houver uma falha durante a gravação.
            temporario = java.nio.file.Files.createTempFile(pasta, ".portugol-", ".tmp");
            java.nio.file.Files.write(temporario, text.getBytes(charset));
            try
            {
                java.nio.file.Files.move(temporario, destino,
                        java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
            catch (java.nio.file.AtomicMoveNotSupportedException excecao)
            {
                java.nio.file.Files.move(temporario, destino, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e)
        {
            String mensagem = e.getMessage() == null ? "" : e.getMessage();
            if (mensagem.contains("Permission denied") || mensagem.contains("Acesso negado"))
            {
                throw new ExcecaoAplicacao("Você não possui permissão de escrita para o arquivo '" + file.getAbsolutePath() + "'", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
            }
            else if (mensagem.contains("enough space on the disk") || mensagem.contains("insuficiente no disco"))
            {
                if (!file.getName().contains("recuperavel"))
                {
                    throw new ExcecaoAplicacao("Não há espaço no disco", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
                }
            }
            else if (mensagem.contains("used by another process") || mensagem.contains("sendo usado por outro processo"))
            {
                throw new ExcecaoAplicacao("O arquivo " + file.getName() + " está sendo utilizado em outro processo. Não é possível alterá-lo!", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
            }
            else if (mensagem.contains("cloud file provider") || mensagem.contains("O provedor do arquivo de nuvem"))
            {
                throw new ExcecaoAplicacao("O seu provedor de arquivos OneDrive está impedindo o Portugol de fazer modificações. Por favor reinicie ele.", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
            }
            throw e;
        }
        finally
        {
            if (temporario != null)
            {
                java.nio.file.Files.deleteIfExists(temporario);
            }
        }
    }

    public static String open(File file) throws Exception
    {
        if (file == null)
        {
            throw new ExcecaoAplicacao("Arquivo nulo não pode ser aberto.", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
        }

        if (file.isDirectory())
        {
            throw new ExcecaoAplicacao("O arquivo solicitado é um diretório.", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
        }

        if (!file.canRead())
        {
            throw new ExcecaoAplicacao("Acesso negado ao arquivo. Você não possuí permissão de leitura para esse arquivo", ExcecaoAplicacao.Tipo.ERRO_USUARIO);
        }

        return read(file);
    }

    private static String read(File file) throws Exception
    {        
        return read(new FileInputStream(file));
    }

    public static String read(InputStream inputStream) throws Exception
    {
        byte[] fileContent = IOUtils.toByteArray(inputStream);
        EncodingDetector detector = new EncodingDetector();
        
        String charset = charsetPadrao;
        charset = detector.detect(inputStream, fileContent);
        String fileText = new String(fileContent, charset);
        inputStream.close();
        return fileText;
    }

    public static String read(InputStream inputStream, String charset) throws Exception
    {
        StringBuilder reading = new StringBuilder();

        try (InputStreamReader isr = new InputStreamReader(inputStream, charset); BufferedReader reader = new BufferedReader(isr))
        {
            int read;
            char[] buffer = new char[4096];
            
            while ((read = reader.read(buffer, 0, buffer.length)) > 0)
            {
                reading.append(buffer, 0, read);
            }
        }
        catch (IOException ex)
        {
            throw new ExcecaoAplicacao(ex, ExcecaoAplicacao.Tipo.ERRO_USUARIO);
            
        }

        return reading.toString().replaceAll("\r\n", "\n");
    }
    
    public static boolean isFilenameValid(String file) {
        File f = new File(file);
        try {
           f.getCanonicalPath();
           return true;
        }
        catch (IOException e) {
           return false;
        }
    }
}
