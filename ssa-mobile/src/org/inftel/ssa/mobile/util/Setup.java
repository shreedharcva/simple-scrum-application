/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package org.inftel.ssa.mobile.util;

/**
 * Class to be customized with app-specific data. The Eclipse plugin will set
 * these values when the project is created.
 */
public class Setup {

    /**
     * The AppEngine app name, used to construct the production service URL
     * below.
     */
    private static final String APP_NAME = "ssa-web";

    /**
     * The URL of the production service.
     */
    public static final String PROD_URL = "http://home.bacamt.com:83/" + APP_NAME;
    // "https://" + APP_NAME + ".appspot.com";

}
