package io.postcodes.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "POST_CODE")
public class PostCode implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @Column(name = "CODE")
    private Integer code;

    public PostCode() {}

    public PostCode(final Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(final Integer code) {
        this.code = code;
    }

}
