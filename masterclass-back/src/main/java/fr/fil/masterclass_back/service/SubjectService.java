package fr.fil.masterclass_back.service;

import fr.fil.masterclass_back.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public String getHelloData() {
        long totalSubjects = subjectRepository.count();

        String nomMatiere = subjectRepository.findById("S1")
                .map(subject -> subject.getSubName())
                .orElse("Matière non trouvée");

        return "Hello World ! La connexion MariaDB fonctionne à merveille. Il y a actuellement "
                + totalSubjects + " matières enregistrées dans la base. (Test ID S1 : " + nomMatiere + ")";
    }
}
