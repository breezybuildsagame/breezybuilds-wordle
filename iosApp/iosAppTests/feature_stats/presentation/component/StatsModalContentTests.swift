//
//  StatsModalContentTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import ViewInspector
import XCTest
@testable import iosApp

final class StatsModalContentTests: XCTestCase
{
    var dimensions: SceneDimensions!
    
    override func setUp() async throws { dimensions = SceneDimensions() }

    func test_when_view_appears__the_outermost_element_is_a_VStack()
    {
        // given
        let sut = StatsModalContent()
        let expectedWidth = mockFrame().width * (315.0 / idealFrame().width)
        let expectedBoarderRadius = mockFrame().height * (8 / idealFrame().height)
        let expectedHorizontalPadding = mockFrame().width * (20 / idealFrame().width)
        let expectedVerticalPadding = mockFrame().height * (40.0 / idealFrame().height)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack())
        XCTAssertEqual(expectedWidth, try sut.inspect().vStack().fixedWidth())
        XCTAssertEqual(expectedHorizontalPadding, try sut.inspect().vStack().padding(.horizontal))
        XCTAssertEqual(expectedVerticalPadding, try sut.inspect().vStack().padding(.vertical))
        XCTAssertEqual(mockFrame().height * (10 / idealFrame().height), try sut.inspect().vStack().shadow().radius)
        XCTAssertEqual(Color.black.opacity(0.25), try sut.inspect().vStack().shadow().color)
    }
    
    func test_when_view_appears__a_Text_view_matching_design_requirements_is_displayed_in_the_VStack_index_1() throws
    {
        // given
        let expectedFont = Font.custom("Roboto-Black", size: mockFrame().height * (20 / idealFrame().height))
        let expectedText = "STATISTICS"
        let sut = StatsModalContent()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedText = try sut.inspect().vStack().text(0)
        
        // then
        XCTAssertEqual(expectedText, try sut.inspect().vStack().text(0).string())
        XCTAssertEqual(try expectedFont.name(), try displayedText.attributes().font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedText.attributes().font().size())
        XCTAssertEqual(.ui.onSurface, try displayedText.attributes().foregroundColor())
    }
    
    func test_when_view_appears_with_stats__an_HStack_matching_design_requirements_is_displayed_in_the_VStack_at_index_1() throws
    {
        // given
        let expectedStats = [
            StatsModalContent.Stat(mockHeadline: "90", mockDescription: "Yah!"),
            StatsModalContent.Stat(mockHeadline: "??", mockDescription: "That's it")
        ]
        let expectedSpacing = mockFrame().width * (5 / idealFrame().width)
        let expectedBottomPadding = mockFrame().height * (15 / idealFrame().height)
        let sut = StatsModalContent(mockStats: expectedStats)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        
        // then
        XCTAssertEqual(.top, try sut.inspect().vStack().hStack(1).alignment())
        XCTAssertEqual(expectedSpacing, try sut.inspect().vStack().hStack(1).spacing())
        XCTAssertEqual(expectedBottomPadding, try sut.inspect().vStack().hStack(1).padding(.bottom))
        XCTAssertEqual(expectedStats.count, try sut.inspect().vStack().hStack(1).forEach(0).count)
        
        try? expectedStats.enumerated().forEach()
        { index, stat in
            XCTAssertEqual(stat, try sut.inspect().vStack().hStack(1).forEach(0).view(StatsModalContent.Stat.self, index).actualView())
        }
    }
    
    func test_when_view_appears_with_guessDistribution__expected_view_is_displayed_in_the_VStack_at_index_2()
    {
        // given
        let expectedGuessDistribution = StatsGuessDistribution(mockTitle: "My Distribution!")
        let sut = StatsModalContent(mockGuessDistribution: expectedGuessDistribution)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        
        // then
        XCTAssertEqual(
            expectedGuessDistribution,
            try sut.inspect().vStack().view(StatsGuessDistribution.self, 2).actualView()
        )
    }
    
    func test_when_view_appears_with_playAgainButton__expected_view_is_displayed_in_the_VStack_at_index_3() throws
    {
        // given
        var tapDidInvoke = false
        let expectedPlayAgainButton = StatsModalContent.PlayAgainButton(mockLabel: "Test Button")
            { tapDidInvoke = true }
        let expectedButtonWidth = mockFrame().width * (275 / idealFrame().width)
        let expectedButtonHeight = mockFrame().height * (50 / idealFrame().height)
        let expectedCornerRadius = mockFrame().width * (4 / idealFrame().width)
        let expectedButtonFont = Font.custom("Roboto-Black", size: mockFrame().height * (14 / idealFrame().height))
        
        // when
        let sut = StatsModalContent(mockPlayAgainButton: expectedPlayAgainButton)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedButton = try sut.inspect().vStack().view(StatsModalContent.PlayAgainButton.self, 3)
        try displayedButton.button().tap()
        
        // then
        XCTAssertEqual(expectedPlayAgainButton, try displayedButton.actualView())
        XCTAssertEqual(expectedButtonWidth, try displayedButton.button().labelView().fixedWidth())
        XCTAssertEqual(expectedButtonHeight, try displayedButton.button().labelView().fixedHeight())
        XCTAssertEqual(.ui.secondary, try displayedButton.button().labelView().background().color().value())
        XCTAssertEqual(expectedCornerRadius, try displayedButton.button().labelView().cornerRadius())
        XCTAssertEqual("Test Button", try displayedButton.button().labelView().text().string())
        XCTAssertEqual(.center, try displayedButton.button().labelView().text().multilineTextAlignment())
        XCTAssertEqual(.ui.surface, try displayedButton.button().labelView().text().attributes().foregroundColor())
        XCTAssertEqual(try expectedButtonFont.name(), try displayedButton.button().labelView().text().attributes().font().name())
        XCTAssertEqual(try expectedButtonFont.size(), try displayedButton.button().labelView().text().attributes().font().size())
        XCTAssertTrue(tapDidInvoke)
    }
}

extension StatsModalContent
{
    init(
        mockStats: [StatsModalContent.Stat] = [],
        mockGuessDistribution: StatsGuessDistribution = StatsGuessDistribution(),
        mockPlayAgainButton: StatsModalContent.PlayAgainButton = StatsModalContent.PlayAgainButton()
    )
    {
        self.init(
            stats: mockStats,
            guessDistribution: mockGuessDistribution,
            playAgainButton:  mockPlayAgainButton
        )
    }
}

extension StatsModalContent.PlayAgainButton
{
    init(mockLabel: String = "", mockTapped: @escaping () -> Void = { })
    {
        self.init(label: mockLabel) { mockTapped() }
    }
}
