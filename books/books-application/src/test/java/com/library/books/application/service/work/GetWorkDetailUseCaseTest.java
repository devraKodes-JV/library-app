package com.library.books.application.service.work;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.library.books.domain.dto.query.WorkWithRelationsDTO;
import com.library.books.domain.exception.WorkNotFoundException;
import com.library.books.domain.port.out.WorkRepository;

class GetWorkDetailUseCaseTest {

    static class FakeWorkRepository implements WorkRepository {
        private WorkWithRelationsDTO detail;

        @Override
        public WorkWithRelationsDTO findByIdWithRelations(Long id) {
            return detail != null && detail.id().equals(id) ? detail : null;
        }

        @Override
        public java.util.Optional<com.library.books.domain.model.Work> findById(Long id) {
            return java.util.Optional.empty();
        }

        @Override
        public List<com.library.books.domain.model.Work> findAll() {
            return List.of();
        }

        @Override
        public List<com.library.books.domain.model.Work> findByCategoryId(Long categoryId) {
            return List.of();
        }

        @Override
        public List<com.library.books.domain.model.Work> findByOriginalLanguageId(Long languageId) {
            return List.of();
        }

        @Override
        public com.library.books.domain.model.Work save(com.library.books.domain.model.Work work) {
            return work;
        }

        @Override
        public void deleteById(Long id) {
        }

        @Override
        public List<com.library.books.domain.model.Work> findByIds(List<Long> ids) {
            return List.of();
        }

        @Override
        public List<com.library.books.domain.model.Work> findByAuthorId(Long authorId) {
            return List.of();
        }

        @Override
        public boolean existsLanguage(Long id) {
            return true;
        }

        @Override
        public boolean existsCategory(Long id) {
            return true;
        }

        @Override
        public void saveWorkAuthor(Long workId, Long authorId) {
        }

        @Override
        public void deleteWorkAuthorsByWorkId(Long workId) {
        }
    }

    @Test
    void getWorkDetail_returnsDetailDTO() {
        FakeWorkRepository workRepository = new FakeWorkRepository();
        workRepository.detail = new WorkWithRelationsDTO(
                1L, "Title", null, "Summary",
                java.time.Instant.now(), java.time.Instant.now(),
                1L, "English", 2L, "Novel", List.of());

        GetWorkDetailUseCase service = new GetWorkDetailUseCase(workRepository);
        var detail = service.execute(1L);

        assertEquals("Title", detail.title());
    }

    @Test
    void getWorkDetail_throwsWhenNotFound() {
        FakeWorkRepository workRepository = new FakeWorkRepository();

        GetWorkDetailUseCase service = new GetWorkDetailUseCase(workRepository);
        assertThrows(WorkNotFoundException.class, () -> service.execute(1L));
    }
}
