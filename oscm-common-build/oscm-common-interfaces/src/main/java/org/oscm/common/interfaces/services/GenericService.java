/*******************************************************************************
 *                                                                              
 *  Copyright FUJITSU LIMITED 2016                                           
 *                                                                                                                                 
 *  Creation Date: Jun 15, 2016                                                      
 *                                                                              
 *******************************************************************************/

package org.oscm.common.interfaces.services;

import java.util.List;

import org.oscm.common.interfaces.data.DataType;
import org.oscm.common.interfaces.data.ParameterType;
import org.oscm.common.interfaces.exceptions.ComponentException;

/**
 * Generic interfaces for CRUD services
 * 
 * @author miethaner
 */
public interface GenericService {

    public interface Create<D extends DataType, P extends ParameterType> {

        /**
         * Creates an entity with the given content and parameters
         * 
         * @param content
         *            the entity content
         * @param params
         *            the creation parameters
         * @return the ID of the created entity
         * @throws ComponentException
         */
        public Long create(D content, P params) throws ComponentException;
    }

    public interface Read<D extends DataType, P extends ParameterType> {

        /**
         * Reads all entities specified by the given parameters
         * 
         * @param params
         *            the read parameters
         * @return the list of entities
         * @throws ComponentException
         */
        public List<D> readAll(P params) throws ComponentException;

        /**
         * Reads the entity specified by the given parameters
         * 
         * @param params
         *            the read parameters
         * @return the specified entity
         * @throws ComponentException
         */
        public D read(P params) throws ComponentException;
    }

    public interface Update<D extends DataType, P extends ParameterType> {

        /**
         * Updates the entity specified by the given parameters with the given
         * content and parameters
         * 
         * @param content
         *            the entity content
         * @param params
         *            the update parameters
         * @return the ID of the created entity
         * @throws ComponentException
         */
        public void update(D content, P params) throws ComponentException;
    }

    public interface Delete<P extends ParameterType> {

        /**
         * Deletes the entity specified in the given parameters
         * 
         * @param params
         *            the deletion parameters
         * @throws ComponentException
         */
        public void delete(P params) throws ComponentException;
    }

    public interface Crud<D extends DataType, P extends ParameterType> extends
            Create<D, P>, Read<D, P>, Update<D, P>, Delete<P> {
    }
}
