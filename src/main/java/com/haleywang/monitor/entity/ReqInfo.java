package com.haleywang.monitor.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.haleywang.monitor.common.ReqException;
import com.haleywang.monitor.common.req.HttpMethod;
import com.haleywang.monitor.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ReqInfo implements Serializable, ReqGroupItem {

    private static final long serialVersionUID = 1L;

	public static class CustomDateDeserializer
			extends StdDeserializer<HttpMethod> {



		public CustomDateDeserializer() {
			this(null);
		}

		public CustomDateDeserializer(Class<?> vc) {
			super(vc);
		}

		@Override
		public HttpMethod deserialize(
				JsonParser jsonparser, DeserializationContext context)
				throws IOException {

			String text = jsonparser.getText();
			try {
				if(EnumUtils.isValidEnum(HttpMethod.class, StringUtils.upperCase(text))) {
					return HttpMethod.valueOf(text);
				}
			} catch (Exception e) {
				throw new ReqException(e);
			}
			return HttpMethod.GET;
		}
	}
    

    @Id
	@GeneratedValue(generator= "JDBC")
    private Long id;

    private String name;

	private String swaggerId;


	@ColumnType(jdbcType = JdbcType.VARCHAR)
	@Column(length = 2048)
    private String url;

	@JsonDeserialize(using = CustomDateDeserializer.class)
	@ColumnType(column = "method", jdbcType = JdbcType.VARCHAR)
	private HttpMethod method = HttpMethod.GET;

	private Long groupId;
    
    @ManyToOne(cascade = CascadeType.REFRESH, optional = true)
    @JoinColumn(name = "groupId", nullable = true)
    private ReqGroup reqGroup;
    
    private Integer sort;
    
    private Date createdOn;
    
    private Date updatedOn;
    
    @Transient
    private Map<String, String> meta = new HashMap<>();
    private int caseIndex;


	public void setReqGroup(ReqGroup reqGroup) {
        if(reqGroup != null) {
            this.groupId = reqGroup.getGroupId();
        }
		this.reqGroup = reqGroup;
	}


	@Override
	public Long getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return name;
	}

	@Override
	public List<ReqGroupItem> getChildren() {
		return new ArrayList<>();
	}


	public Map<String, String> getMeta() {
		if(meta == null) {
            meta = new HashMap<>();
        }
        return meta;
	}

}
