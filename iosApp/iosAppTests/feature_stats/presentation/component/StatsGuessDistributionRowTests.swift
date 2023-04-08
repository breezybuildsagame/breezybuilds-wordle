//
//  StatsGuessDistributionTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import ViewInspector
import XCTest
@testable import iosApp

final class StatsGuessDistributionRowTests: XCTestCase
{
    func test_when_view_appears__the_outermost_element_is_an_HStack()
    {
        // given
        let sut = StatsGuessDistribution.Row()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().hStack())
    }
    
    func test_when_view_appears_with_round__the_HStack_contains_a_Text_view_matching_expected_round()
    {
        // given
        let sut = StatsGuessDistribution.Row(mockRound: "1")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        
        // then
        XCTAssertEqual(mockFrame().width * (5 / idealFrame().width), try sut.inspect().hStack().spacing())
        XCTAssertEqual(.center, try sut.inspect().hStack().alignment())
        XCTAssertNoThrow(try sut.inspect().hStack().text(0))
        XCTAssertEqual("1", try sut.inspect().hStack().text(0).string())
    }
    
    func test_when_view_appears_with_round__the_Text_view_matches_design_requirements() throws
    {
        // given
        let expectedFont = Font.custom(
            "Roboto-Regular",
            size: mockFrame().height * (20 / idealFrame().height)
        )
        let sut = StatsGuessDistribution.Row(mockRound: "1")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let displayedRow = try sut.inspect().find(StatsGuessDistribution.Row.self)
        let displayedTextAttributes = try displayedRow.hStack().text(0).attributes()
        
        // then
        XCTAssertEqual(Color.ui.onSurface, try displayedRow.hStack().foregroundColor())
        XCTAssertEqual(try expectedFont.name(), try displayedTextAttributes.font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedTextAttributes.font().size())
        XCTAssertEqual(.trailing, try displayedRow.hStack().text(0).multilineTextAlignment())
    }
    
    func test_when_view_appears_with_correctGuessCount__the_HStack_contains_a_Text_view_at_index_1()
    {
        // given
        let sut = StatsGuessDistribution.Row(mockCorrectGuessCount: "99")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(SceneDimensions()))
        
        // then
        XCTAssertNoThrow(try sut.inspect().hStack().text(1))
        XCTAssertEqual("99", try sut.inspect().hStack().text(1).string())
    }
    
    func test_when_view_appears_with_correctGuessCount__the_Text_view_matches_design_requirements() throws
    {
        // given
        let expectedFont = Font.custom(
            "Roboto-Regular",
            size: mockFrame().height * (20 / idealFrame().height)
        )
        let expectedHorizontalPadding = mockFrame().width * (5 / idealFrame().width)
        let sut = StatsGuessDistribution.Row(mockCorrectGuessCount: "99")
        let dimensions = SceneDimensions()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(width: mockFrame().width, height: mockFrame().height)
        let displayedRow = try sut.inspect().find(StatsGuessDistribution.Row.self)
        let displayedTextAttributes = try displayedRow.hStack().text(1).attributes()
        
        // then
        XCTAssertEqual(try expectedFont.name(), try displayedTextAttributes.font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedTextAttributes.font().size())
        XCTAssertEqual(.trailing, try displayedRow.hStack().text(1).multilineTextAlignment())
        XCTAssertEqual(expectedHorizontalPadding, try displayedRow.hStack().text(1).padding(.horizontal))
        XCTAssertEqual(.ui.error, try displayedRow.hStack().text(1).background().color().value())
    }
}

extension StatsGuessDistribution.Row
{
    init(mockRound: String = "", mockCorrectGuessCount: String = "")
    {
        self.init(round: mockRound, correctGuessCount: mockCorrectGuessCount)
    }
}
