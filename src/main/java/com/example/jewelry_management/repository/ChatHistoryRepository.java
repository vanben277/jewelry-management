package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.ChatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatHistoryRepository extends JpaRepository<ChatHistory, Long> {
    List<ChatHistory> findTop10BySessionIdOrderByCreatedAtDesc(String sessionId);

    List<ChatHistory> findByAccountIdOrderByCreatedAtAsc(Integer accountId);

    @Query(value = "SELECT user_query from chat_history where 'TEXT' order by created_at desc " +
            "limit 50", nativeQuery = true)
    List<String> findRecentCustomerQueries();

    List<ChatHistory> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    List<ChatHistory> findByAccountIdOrderByCreatedAtDesc(Integer accountId);

    @Query("SELECT c.sessionId FROM ChatHistory c WHERE c.account.id = :accountId GROUP BY c.sessionId ORDER BY MAX(c.createdAt) DESC")
    List<String> findDistinctSessionIdByAccountId(@Param("accountId") Integer accountId);

    @Query(value = """
            SELECT
                ch.session_id,
                (
                    SELECT ch2.user_query
                    FROM chat_history ch2
                    WHERE ch2.session_id = ch.session_id
                    ORDER BY ch2.created_at DESC
                    LIMIT 1
                ) AS last_message,
                MAX(ch.created_at) AS last_message_at,
                COUNT(*) AS message_count
            FROM chat_history ch
            WHERE ch.account_id = :accountId
            GROUP BY ch.session_id
            ORDER BY last_message_at DESC
            """, nativeQuery = true)
    List<Object[]> findSessionSummaryByAccountId(@Param("accountId") Integer accountId);

    @Query(value = """
        SELECT
            ch.session_id,
            (
                SELECT ch2.user_query
                FROM chat_history ch2
                WHERE ch2.session_id = ch.session_id
                ORDER BY ch2.created_at DESC
                LIMIT 1
            ) AS last_message,
            MAX(ch.created_at) AS last_message_at,
            COUNT(*) AS message_count,
            a.id AS account_id,
            CONCAT(a.firstname, ' ', a.lastname) AS full_name,
            a.avatar AS avatar
        FROM chat_history ch
        JOIN account a ON ch.account_id = a.id
        WHERE ch.account_id IS NOT NULL
        GROUP BY ch.session_id, a.id, a.firstname, a.lastname, a.avatar
        ORDER BY last_message_at DESC
        """, nativeQuery = true)
    List<Object[]> findAllSessionSummaryForAdmin();
}
