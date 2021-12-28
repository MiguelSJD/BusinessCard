package br.com.dio.businesscard.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import br.com.dio.businesscard.getOrAwaitValue
import kotlinx.coroutines.test.runBlockingTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class BusinessCardDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database:AppDataBase
    private lateinit var  dao:BusinessCardDao

    @Before
    fun setup(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDataBase::class.java).allowMainThreadQueries().build()
        dao = database.businessDao()
    }

    @After
    fun teardown(){
        database.close()
    }

    private val businessCardJuliana = BusinessCard(1, "Juliana", "Invillia", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF")

    @Test
    fun insertBusinessCard() = runBlockingTest {
        dao.insert(businessCardJuliana)

        val allBusinessCard = dao.getAll().getOrAwaitValue()

        assertThat(allBusinessCard).contains(BusinessCard(1, "Juliana", "Invillia", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF"))
    }

    @Test
    fun deleteBusinessCard() = runBlockingTest {
        dao.insert(businessCardJuliana)

        dao.deleteCard(businessCardJuliana)

        val allBusinessCard = dao.getAll().getOrAwaitValue()

        assertThat(allBusinessCard).doesNotContain(BusinessCard(1, "Juliana", "Invillia", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF"))
    }

    @Test
    fun getAllBusinessCard() = runBlockingTest {
        val businessCardMarcio = BusinessCard(2, "Marcio", "Digital Inovation One", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF")

        dao.insert(businessCardJuliana)
        dao.insert(businessCardMarcio)

        val allBusinessCard = dao.getAll().getOrAwaitValue()

        assertThat(allBusinessCard).contains(BusinessCard(1, "Juliana", "Invillia", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF"))
        assertThat(allBusinessCard).contains(BusinessCard(2, "Marcio", "Digital Inovation One", "(99) 9999-9999", "juliana@gmail.com", "#FF00FF"))
    }
}