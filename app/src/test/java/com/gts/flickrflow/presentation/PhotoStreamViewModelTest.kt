package com.gts.flickrflow.presentation

import org.junit.Test
import org.junit.Rule
import org.junit.Before
import org.junit.Assert

import androidx.arch.core.executor.testing.InstantTaskExecutorRule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever

import com.gts.flickrflow.common.Result
import com.gts.flickrflow.domain.model.Photo
import com.gts.flickrflow.domain.usecase.RetrievePhotosFromDbUseCase
import com.gts.flickrflow.presentation.model.PhotoViewItem
import com.gts.flickrflow.presentation.model.toPresentationModel

class PhotoStreamViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PhotoStreamViewModel
    private val mockRetrievePhotosFromDbUseCase: RetrievePhotosFromDbUseCase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = PhotoStreamViewModel(mockRetrievePhotosFromDbUseCase)
    }

    @Test
    fun retrievePhotosFromDb_sendsListOfPhotos() {
        val photo = Photo("id", "secret", "server", "farm")
        val expected = Result.Success(listOf(photo))
        runBlocking { whenever(mockRetrievePhotosFromDbUseCase.invoke()).thenReturn(expected) }
        // when
        viewModel.retrievePhotosFromDb()
        // then
        val photoList: List<PhotoViewItem>? = LiveDataTestUtil.getValue(viewModel.photosFromDb)
        Assert.assertNotNull(photoList)
        Assert.assertEquals(expected.data.map { it.toPresentationModel() }, photoList)
    }
}