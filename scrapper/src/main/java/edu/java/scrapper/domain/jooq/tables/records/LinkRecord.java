/*
 * This file is generated by jOOQ.
 */

package edu.java.scrapper.domain.jooq.tables.records;

import edu.java.scrapper.domain.jooq.tables.Link;
import jakarta.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.OffsetDateTime;
import javax.annotation.processing.Generated;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "https://www.jooq.org",
        "jOOQ version:3.16.6"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LinkRecord extends UpdatableRecordImpl<LinkRecord> implements Record6<Integer, String, OffsetDateTime, OffsetDateTime, String, Long> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>LINK.ID</code>.
     */
    public void setId(@NotNull Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>LINK.ID</code>.
     */
    @NotNull
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>LINK.URL</code>.
     */
    public void setUrl(@NotNull String value) {
        set(1, value);
    }

    /**
     * Getter for <code>LINK.URL</code>.
     */
    @jakarta.validation.constraints.NotNull
    @Size(max = 1000000000)
    @NotNull
    public String getUrl() {
        return (String) get(1);
    }

    /**
     * Setter for <code>LINK.UPDATED_AT</code>.
     */
    public void setUpdatedAt(@NotNull OffsetDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>LINK.UPDATED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getUpdatedAt() {
        return (OffsetDateTime) get(2);
    }

    /**
     * Setter for <code>LINK.LAST_CHECKED_AT</code>.
     */
    public void setLastCheckedAt(@NotNull OffsetDateTime value) {
        set(3, value);
    }

    /**
     * Getter for <code>LINK.LAST_CHECKED_AT</code>.
     */
    @jakarta.validation.constraints.NotNull
    @NotNull
    public OffsetDateTime getLastCheckedAt() {
        return (OffsetDateTime) get(3);
    }

    /**
     * Setter for <code>LINK.LAST_COMMIT_SHA</code>.
     */
    public void setLastCommitSha(@Nullable String value) {
        set(4, value);
    }

    /**
     * Getter for <code>LINK.LAST_COMMIT_SHA</code>.
     */
    @Size(max = 1000000000)
    @Nullable
    public String getLastCommitSha() {
        return (String) get(4);
    }

    /**
     * Setter for <code>LINK.ANSWERS_COUNT</code>.
     */
    public void setAnswersCount(@Nullable Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>LINK.ANSWERS_COUNT</code>.
     */
    @Nullable
    public Long getAnswersCount() {
        return (Long) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    @Override
    @NotNull
    public Row6<Integer, String, OffsetDateTime, OffsetDateTime, String, Long> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    @Override
    @NotNull
    public Row6<Integer, String, OffsetDateTime, OffsetDateTime, String, Long> valuesRow() {
        return (Row6) super.valuesRow();
    }

    @Override
    @NotNull
    public Field<Integer> field1() {
        return Link.LINK.ID;
    }

    @Override
    @NotNull
    public Field<String> field2() {
        return Link.LINK.URL;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field3() {
        return Link.LINK.UPDATED_AT;
    }

    @Override
    @NotNull
    public Field<OffsetDateTime> field4() {
        return Link.LINK.LAST_CHECKED_AT;
    }

    @Override
    @NotNull
    public Field<String> field5() {
        return Link.LINK.LAST_COMMIT_SHA;
    }

    @Override
    @NotNull
    public Field<Long> field6() {
        return Link.LINK.ANSWERS_COUNT;
    }

    @Override
    @NotNull
    public Integer component1() {
        return getId();
    }

    @Override
    @NotNull
    public String component2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime component3() {
        return getUpdatedAt();
    }

    @Override
    @NotNull
    public OffsetDateTime component4() {
        return getLastCheckedAt();
    }

    @Override
    @Nullable
    public String component5() {
        return getLastCommitSha();
    }

    @Override
    @Nullable
    public Long component6() {
        return getAnswersCount();
    }

    @Override
    @NotNull
    public Integer value1() {
        return getId();
    }

    @Override
    @NotNull
    public String value2() {
        return getUrl();
    }

    @Override
    @NotNull
    public OffsetDateTime value3() {
        return getUpdatedAt();
    }

    @Override
    @NotNull
    public OffsetDateTime value4() {
        return getLastCheckedAt();
    }

    @Override
    @Nullable
    public String value5() {
        return getLastCommitSha();
    }

    @Override
    @Nullable
    public Long value6() {
        return getAnswersCount();
    }

    @Override
    @NotNull
    public LinkRecord value1(@NotNull Integer value) {
        setId(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value2(@NotNull String value) {
        setUrl(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value3(@NotNull OffsetDateTime value) {
        setUpdatedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value4(@NotNull OffsetDateTime value) {
        setLastCheckedAt(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value5(@Nullable String value) {
        setLastCommitSha(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord value6(@Nullable Long value) {
        setAnswersCount(value);
        return this;
    }

    @Override
    @NotNull
    public LinkRecord values(@NotNull Integer value1, @NotNull String value2, @NotNull OffsetDateTime value3, @NotNull OffsetDateTime value4, @Nullable String value5, @Nullable Long value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LinkRecord
     */
    public LinkRecord() {
        super(Link.LINK);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    @ConstructorProperties({ "id", "url", "updatedAt", "lastCheckedAt", "lastCommitSha", "answersCount" })
    public LinkRecord(@NotNull Integer id, @NotNull String url, @NotNull OffsetDateTime updatedAt, @NotNull OffsetDateTime lastCheckedAt, @Nullable String lastCommitSha, @Nullable Long answersCount) {
        super(Link.LINK);

        setId(id);
        setUrl(url);
        setUpdatedAt(updatedAt);
        setLastCheckedAt(lastCheckedAt);
        setLastCommitSha(lastCommitSha);
        setAnswersCount(answersCount);
    }

    /**
     * Create a detached, initialised LinkRecord
     */
    public LinkRecord(edu.java.scrapper.domain.jooq.tables.pojos.Link value) {
        super(Link.LINK);

        if (value != null) {
            setId(value.getId());
            setUrl(value.getUrl());
            setUpdatedAt(value.getUpdatedAt());
            setLastCheckedAt(value.getLastCheckedAt());
            setLastCommitSha(value.getLastCommitSha());
            setAnswersCount(value.getAnswersCount());
        }
    }
}
