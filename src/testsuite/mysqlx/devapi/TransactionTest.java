/*
  Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.

  The MySQL Connector/J is licensed under the terms of the GPLv2
  <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most MySQL Connectors.
  There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
  this software, see the FLOSS License Exception
  <http://www.mysql.com/about/legal/licensing/foss-exception.html>.

  This program is free software; you can redistribute it and/or modify it under the terms
  of the GNU General Public License as published by the Free Software Foundation; version 2
  of the License.

  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with this
  program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
  Floor, Boston, MA 02110-1301  USA

 */

package testsuite.mysqlx.devapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysql.cj.api.x.Collection;
import com.mysql.cj.api.x.DatabaseObject.DbObjectStatus;
import com.mysql.cj.api.x.Schema;
import com.mysql.cj.api.x.Session;
import com.mysql.cj.core.exceptions.MysqlErrorNumbers;
import com.mysql.cj.mysqlx.MysqlxError;
import com.mysql.cj.mysqlx.devapi.SessionImpl;

public class TransactionTest extends BaseDevApiTest {
    protected Collection collection;

    @Before
    public void setupCollectionTest() {
        setupTestSession();
        dropCollection("txTest");
        this.collection = this.schema.createCollection("txTest");
    }

    @After
    public void teardownCollectionTest() {
        dropCollection("txTest");
        destroyTestSession();
    }

    @Test
    public void basicRollback() {
        this.collection.add("{}").add("{}").execute();

        this.session.startTransaction();
        this.collection.add("{}").add("{}").execute();
        assertEquals(4, this.collection.find().execute().all().count());
        this.session.rollback();

        assertEquals(2, this.collection.find().execute().all().count());
    }

    @Test
    public void basicCommit() {
        this.collection.add("{}").add("{}").execute();

        this.session.startTransaction();
        this.collection.add("{}").add("{}").execute();
        assertEquals(4, this.collection.find().execute().all().count());
        this.session.commit();

        assertEquals(4, this.collection.find().execute().all().count());
    }
}
