package com.example.demo.controller;

import com.example.demo.dto.TopicoDTO;
import com.example.demo.model.Topico;
import com.example.demo.repository.TopicoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<?> createTopico(@Valid @RequestBody TopicoDTO topicoDTO) {
        Optional<Topico> existingTopico = topicoRepository.findByTituloAndCurso(topicoDTO.getTitulo(), topicoDTO.getCurso());
        if (existingTopico.isPresent()) {
            //status code 400
            return ResponseEntity.badRequest().body("Tópico com o mesmo título e curso já existe.");
        }

        Topico topico = new Topico();
        topico.setTitulo(topicoDTO.getTitulo());
        topico.setMensagem(topicoDTO.getMensagem());
        topico.setAutor(topicoDTO.getAutor());
        topico.setCurso(topicoDTO.getCurso());

        Topico savedTopico = topicoRepository.save(topico);
        //Status code 200
        return ResponseEntity.ok(savedTopico);
    }

    @GetMapping
    public ResponseEntity<?> getAllTopicos() {
        List<Topico> topicos = topicoRepository.findAll();
        if (topicos.isEmpty()) {
            return ResponseEntity.status(404).body("Não existem tópicos cadastrados.");
        }
        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicoById(@PathVariable Long id) {
        Optional<Topico> topico = topicoRepository.findById(id);
        if (topico.isPresent()) {
            return ResponseEntity.ok(topico.get());
        }
        return ResponseEntity.status(404).body("Tópico não encontrado.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTopico(@PathVariable Long id, @Valid @RequestBody TopicoDTO topicoDTO) {
        Optional<Topico> existingTopico = topicoRepository.findById(id);
        if (existingTopico.isPresent()) {
            Topico topico = existingTopico.get();
            topico.setTitulo(topicoDTO.getTitulo());
            topico.setMensagem(topicoDTO.getMensagem());
            topico.setAutor(topicoDTO.getAutor());
            topico.setCurso(topicoDTO.getCurso());

            topicoRepository.save(topico);
            //Sttaus code 200
            return ResponseEntity.ok(topico);
        }
        return ResponseEntity.status(404).body("Tópico não encontrado.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopico(@PathVariable Long id) {
        Optional<Topico> existingTopico = topicoRepository.findById(id);
        if (existingTopico.isPresent()) {
            topicoRepository.delete(existingTopico.get());
            return ResponseEntity.ok("Tópico deletado com sucesso.");
        }
        return ResponseEntity.status(404).body("Tópico não encontrado.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
