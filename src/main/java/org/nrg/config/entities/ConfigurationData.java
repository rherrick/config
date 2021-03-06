/*
 * org.nrg.config.entities.ConfigurationData
 * XNAT http://www.xnat.org
 * Copyright (c) 2014, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 *
 * Last modified 9/4/13 4:37 PM
 */
package org.nrg.config.entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.nrg.framework.orm.hibernate.AbstractHibernateEntity;
import org.nrg.framework.orm.hibernate.annotations.Auditable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Auditable
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "nrg")
public class ConfigurationData  extends AbstractHibernateEntity{
	
	public static final int MAX_FILE_LENGTH = 10485760; //max size for postgres, see htup.h #define MaxAttrSize (10 * 1024 * 1024)
    private static final long serialVersionUID = -4349275622581910917L;

    private Set<Configuration> configurations;

	private String contents;
	
	@Column(length=MAX_FILE_LENGTH)
	//@Column(columnDefinition="TEXT")//if you need > 10MB files uncomment this, comment out length and adjust MAX_FILE_LENGTH 
	public String getContents() {
		return contents;
	}
	
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	@OneToMany(fetch = FetchType.EAGER)
	public Set<Configuration> getConfigurations() {
		return configurations;
	}
	
	public void setConfigurations(Set<Configuration> configurations) {
		this.configurations = configurations;
	}

    /**
     * This method looks only at the contents of the configuration data and ignores the associated configurations.
     * @param object    The object to which this object should be compared.
     * @return True if the contents of the configuration data object are equal.
     */
    @Override
    public boolean equals(final Object object) {
        return this == object || object instanceof ConfigurationData && getContents().equals(((ConfigurationData) object).getContents());
    }

    @Override
    public int hashCode() {
        return getContents().hashCode();
    }
}
