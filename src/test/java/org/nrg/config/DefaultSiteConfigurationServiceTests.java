package org.nrg.config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nrg.config.exceptions.ConfigServiceException;
import org.nrg.config.exceptions.DuplicateConfigurationDetectedException;
import org.nrg.config.exceptions.SiteConfigurationFileNotFoundException;
import org.nrg.config.services.SiteConfigurationService;
import org.nrg.config.services.impl.DefaultSiteConfigurationService;
import org.nrg.config.util.TestDBUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class DefaultSiteConfigurationServiceTests {
	
	private List<String> savedConfigFileLocations;
	
	@Before
	public void setUp() {
		_testDBUtils.cleanDb();
		savedConfigFileLocations = new ArrayList<String>(((DefaultSiteConfigurationService) _service).getConfigFilesLocations());
	}
	
	@After
	public void tearDown() {
		((DefaultSiteConfigurationService) _service).setConfigFilesLocations(savedConfigFileLocations);
	}
	
    @Test
    public void initSiteConfigurationSuccess() throws ConfigServiceException {
    	Properties props = _service.getSiteConfiguration();
    	assertNotNull(props);
    	assertNotNull(props.getProperty("prop1"));
    	assertNotNull(props.getProperty("foo.prop1"));
    	assertNull(props.getProperty("foo.prop2"));
    }
    
    @Test
    public void initSiteConfigurationSuccessWithAdditionalPropertiesOnSecondLaunch() throws ConfigServiceException {
    	Properties props = _service.getSiteConfiguration();
    	assertNull(props.getProperty("prop2"));
    	List<String> mockConfigFileLocations = new ArrayList<String>(savedConfigFileLocations);
    	mockConfigFileLocations.set(0, mockConfigFileLocations.get(0).concat("/additionalProperties"));
    	((DefaultSiteConfigurationService) _service).setConfigFilesLocations(mockConfigFileLocations);
   		props = _service.getSiteConfiguration();
    	assertNotNull(props.getProperty("prop2"));
    }
    
    @Test(expected=SiteConfigurationFileNotFoundException.class)
    public void initSiteConfigurationFailsWhenNoSiteConfigIsFound() throws ConfigServiceException {
    	List<String> mockConfigFileLocations = Arrays.asList("/bridge/to/nowhere");
    	((DefaultSiteConfigurationService) _service).setConfigFilesLocations(mockConfigFileLocations);
   		_service.getSiteConfiguration();
    }
    
    @Test(expected=DuplicateConfigurationDetectedException.class)
    public void initSiteConfigurationFailsWhenDuplicateSiteConfigFileIsFound() throws ConfigServiceException {
    	List<String> mockConfigFileLocations = new ArrayList<String>(savedConfigFileLocations);
    	mockConfigFileLocations.add(mockConfigFileLocations.get(0));
    	((DefaultSiteConfigurationService) _service).setConfigFilesLocations(mockConfigFileLocations);
   		_service.getSiteConfiguration();
    }
    
    @Test(expected=DuplicateConfigurationDetectedException.class)
    public void initSiteConfigurationFailsWhenDuplicateCustomConfigFileIsFound() throws ConfigServiceException {
    	List<String> mockConfigFileLocations = new ArrayList<String>(savedConfigFileLocations);
    	mockConfigFileLocations.add(mockConfigFileLocations.get(0).concat("/duplicateFiles"));
    	((DefaultSiteConfigurationService) _service).setConfigFilesLocations(mockConfigFileLocations);
   		_service.getSiteConfiguration();
    }
    
    @Test(expected=DuplicateConfigurationDetectedException.class)
    public void initSiteConfigurationFailsWhenDuplicateCustomConfigPropertyIsFound() throws ConfigServiceException {
    	List<String> mockConfigFileLocations = new ArrayList<String>(savedConfigFileLocations);
    	mockConfigFileLocations.add(mockConfigFileLocations.get(0).concat("/duplicateProperties"));
    	((DefaultSiteConfigurationService) _service).setConfigFilesLocations(mockConfigFileLocations);
   		_service.getSiteConfiguration();
    }
    
    @Test
    public void setSiteConfiguration() throws ConfigServiceException {
    	
    	Properties props = _service.getSiteConfiguration();
    	assertNull(props.getProperty("foo.prop2"));
    	props.setProperty("foo.prop2", "val2");
    	_service.setSiteConfiguration(ADMIN_USER, props);
    	props = _service.getSiteConfiguration();
    	assertEquals(props.getProperty("foo.prop2"), "val2");
    }
    
    @Test
    public void getSiteConfigurationProperty() throws ConfigServiceException {
    	
    	assertEquals(_service.getSiteConfigurationProperty("foo.prop1"), "val1");
    }
    
    @Test
    public void setSiteConfigurationProperty() throws ConfigServiceException {
    	
    	assertEquals(_service.getSiteConfigurationProperty("foo.prop1"), "val1");
    	_service.setSiteConfigurationProperty(ADMIN_USER, "foo.prop1", "val2");
    	assertEquals(_service.getSiteConfigurationProperty("foo.prop1"), "val2");
    }
    
    @Inject
    private SiteConfigurationService _service;
    
    @Inject
    private TestDBUtils _testDBUtils;
    
    private static final String ADMIN_USER = "admin";
}