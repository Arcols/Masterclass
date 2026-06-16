package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.repository.SubjectRepository;
import org.springframework.stereotype.Service;
import fr.fil.masterclass_back.model.Subject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public String getHelloData() {
        long totalSubjects = subjectRepository.count();

        List<Subject> subjects = subjectRepository.findAll();

        String listeNoms = subjects.stream()
                .map(subject -> subject.getSubName())
                .collect(Collectors.joining(", "));

        return "Hello World ! La connexion MariaDB fonctionne à merveille. Il y a actuellement "
                + totalSubjects + " matières enregistrées : " + listeNoms;
    }
}
