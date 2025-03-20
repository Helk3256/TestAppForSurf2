package com.example.TestAppForSurf

    fun convertBooksToVolumes(books: List<Book>): List<Volume> {
        return books.map { book ->
            Volume(
                id = book.id,
                volumeInfo = VolumeInfo(
                    title = book.title,
                    authors = book.authors,
                    publisher = "Unknown Publisher", // Значение по умолчанию
                    publishedDate = book.publishedDate,
                    description = book.description,
                    industryIdentifiers = emptyList(), // Значение по умолчанию
                    readingModes = ReadingModes(false, false), // Значение по умолчанию
                    pageCount = 0, // Значение по умолчанию
                    printType = "Unknown Print Type", // Значение по умолчанию
                    categories = emptyList(), // Значение по умолчанию
                    maturityRating = "Unknown Maturity Rating", // Значение по умолчанию
                    allowAnonLogging = false, // Значение по умолчанию
                    contentVersion = "Unknown Content Version", // Значение по умолчанию
                    panelizationSummary = PanelizationSummary(false, false), // Значение по умолчанию
                    imageLinks = book.imageLinks,
                    language = "Unknown Language", // Значение по умолчанию
                    previewLink = "Unknown Preview Link", // Значение по умолчанию
                    infoLink = "Unknown Info Link", // Значение по умолчанию
                    canonicalVolumeLink = "Unknown Canonical Volume Link" // Значение по умолчанию
                )
            )
        }
    }