package com.aws.s3spring.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Date;


@Service
public class AWSS3Service implements FileService{

    @Autowired
    public AmazonS3 s3client;

    @Override
    public String uploadFile(MultipartFile file) {

        System.out.println("Iniciando processo de envio de arquivo para o bucket-s3");
        //Pegando a extensão do arquivo a ser upado
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        //Criando um ID Unico pro arquivo
        //String key = UUID.randomUUID().toString() + "." + extension;
        Date date = new Date();

        String key = "Arquivo enviado via API " + date.getDate();
        //Criando metadata
        ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

        try {
            //Tenta fazer o put lá na aws, aqui passa o nome do bucket, o id dele, o arquivo e o metadata
            s3client.putObject("devbaiano-bucket",key,file.getInputStream(), metadata);
        }catch (IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Ocorreu um erro durante o upload do seu arquivo :( ");
        }
        System.out.println("Processo finalizado com sucesso");
        return "Seu arquivo foi enviado com sucesso, parabéns, você pode checar o seu bucket e conferir aqui: " + s3client.getUrl("devbaiano-bucket",key);
    }
}
