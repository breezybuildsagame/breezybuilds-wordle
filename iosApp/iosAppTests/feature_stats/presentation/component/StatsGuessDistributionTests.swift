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

final class StatsGuessDistributionTests: XCTestCase
{
    var dimensions: SceneDimensions!
    
    override func setUp() async throws { dimensions = SceneDimensions() }
    
    func test_when_view_appears__the_outermost_element_is_a_VStack()
    {
        // given
        let expectedStackSpacing = mockFrame().height * (5 / idealFrame().height)
        let expectedBottomPadding = mockFrame().height * (40 / idealFrame().height)
        let sut = StatsGuessDistribution()
        
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
        XCTAssertEqual(expectedStackSpacing, try sut.inspect().vStack().spacing())
        XCTAssertEqual(expectedBottomPadding, try sut.inspect().vStack().padding(.bottom))
        XCTAssertEqual(.leading, try sut.inspect().vStack().alignment())
        XCTAssertEqual(.infinity, try sut.inspect().vStack().flexFrame().maxWidth)
    }
    
    func test_when_view_appears_with_title__a_Text_view_the_first_view_within_the_VStack()
    {
        // given
        let sut = StatsGuessDistribution(mockTitle: "Test Title")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack().text(0))
        XCTAssertEqual("Test Title", try sut.inspect().vStack().text(0).string())
    }
    
    func test_when_view_appears_with_title__the_Text_view_matches_design_requirements() throws
    {
        // given
        let expectedFont = Font.custom(
            "Roboto-Black",
            size: mockFrame().height * (20 / idealFrame().height)
        )
        let expectedBottomPadding = mockFrame().height * (20 / idealFrame().height)
        let sut = StatsGuessDistribution(mockTitle: "Test Title")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedFont = try sut.inspect().vStack().text(0)
        
        // then
        XCTAssertEqual(try expectedFont.name(), try displayedFont.attributes().font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedFont.attributes().font().size())
        XCTAssertEqual(.center, try displayedFont.multilineTextAlignment())
        XCTAssertEqual(.ui.onSurface, try displayedFont.attributes().foregroundColor())
        XCTAssertEqual(expectedBottomPadding, try displayedFont.padding(.bottom))
    }
    
    func test_when_view_appears_with_rows__expected_rows_are_displayed()
    {
        // given
        let expectedRows = [
            StatsGuessDistribution.Row(round: "1", correctGuessCount: "10"),
            StatsGuessDistribution.Row(round: "12", correctGuessCount: "153")
        ]
        let sut = StatsGuessDistribution(mockRows: expectedRows)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        
        // then
        XCTAssertEqual(expectedRows.count, try sut.inspect().vStack().forEach(1).count)
        try? expectedRows.enumerated().forEach()
        { index, row in
            XCTAssertEqual(row, try sut.inspect().vStack().forEach(1).view(StatsGuessDistribution.Row.self, index).actualView())
        }
    }
}

extension StatsGuessDistribution
{
    init(mockTitle: String = "", mockRows: [StatsGuessDistribution.Row] = [])
    {
        self.init(title: mockTitle, rows: mockRows)
    }
}
