/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.impl.handler;

import org.jboss.arquillian.impl.Validate;
import org.jboss.arquillian.impl.context.ClassContext;
import org.jboss.arquillian.impl.event.EventHandler;
import org.jboss.arquillian.impl.event.type.SuiteEvent;
import org.jboss.arquillian.spi.DeployableContainer;

/**
 * A Handler for restarting the {@link DeployableContainer} for every X deployments.<br/>
 * <br/>
 *
 *  <b>Imports:</b><br/>
 *   {@link DeployableContainer}<br/>
 *   
 * @author <a href="mailto:aslak@conduct.no">Aslak Knutsen</a>
 * @version $Revision: $
 * @see DeployableContainer
 */
public class ContainerRestarter implements EventHandler<ClassContext, SuiteEvent>
{
   private static final int MAX_DEPLOYMENTS_BEFORE_RESTART = 5;
   
   private int deploymentCount = 0;
   
   /* (non-Javadoc)
    * @see org.jboss.arquillian.impl.event.EventHandler#callback(java.lang.Object, java.lang.Object)
    */
   @Override
   public void callback(ClassContext context, SuiteEvent event) throws Exception
   {
      DeployableContainer container = context.get(DeployableContainer.class);
      Validate.stateNotNull(container, "No " + DeployableContainer.class.getName() + " found in context");

      if(deploymentCount == getMaxDeployments(context) -1)
      {
         container.stop();
         container.start();
         
         deploymentCount = 0;
      }
      deploymentCount++;
   }
   
   private int getMaxDeployments(ClassContext context)
   {
      //context.get(Configuration.class).getMaxDeploymentsBeforeRestart();
      return MAX_DEPLOYMENTS_BEFORE_RESTART;
   }
}
