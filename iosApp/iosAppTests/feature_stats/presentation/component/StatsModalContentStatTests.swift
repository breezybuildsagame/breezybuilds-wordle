//
//  StatsModalStatTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/8/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import ViewInspector
import XCTest
@testable import iosApp

final class StatsModalContentStatTests: XCTestCase
{
    var dimensions: SceneDimensions!
    
    override func setUp() async throws { dimensions = SceneDimensions() }

    func test_when_view_appears__the_outermost_element_is_a_VStack()
    {
        // padding bottom: 15
        // given
        let sut = StatsModalContent.Stat()
        let expectedBottomPadding = mockFrame().height * (15.0 / idealFrame().height)
        
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
        XCTAssertEqual(expectedBottomPadding, try sut.inspect().vStack().padding(.bottom))
    }
    
    func test_when_view_appears_with_headline__the_first_element_inside_the_VStack_is_a_Text_view()
    {
        // given
        let sut = StatsModalContent.Stat(mockHeadline: "20")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack().text(0))
        XCTAssertEqual("20", try sut.inspect().vStack().text(0).string())
    }
    
    func test_when_view_appears_with_headline__the_Text_view_matches_design_requirements() throws
    {
        // given
        let expectedFont = Font.custom(
            "Roboto-Regular",
            size: mockFrame().height * (40 / idealFrame().height)
        )
        let expectedLineSpacing = mockFrame().height * (47 / idealFrame().height)
        let sut = StatsModalContent.Stat(mockHeadline: "20")
        
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
        XCTAssertEqual(try expectedFont.name(), try displayedText.attributes().font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedText.attributes().font().size())
        XCTAssertEqual(expectedLineSpacing, try displayedText.lineSpacing())
        XCTAssertEqual(.ui.onSurface, try displayedText.attributes().foregroundColor())
        XCTAssertEqual(.center, try displayedText.multilineTextAlignment())
    }
    
    func test_when_view_appears_with_description__the_second_element_inside_the_VStack_is_a_Text_view()
    {
        // given
        let sut = StatsModalContent.Stat(mockDescription: "Max Streak")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        
        // then
        XCTAssertNoThrow(try sut.inspect().vStack().text(1))
        XCTAssertEqual("Max Streak", try sut.inspect().vStack().text(1).string())
    }
    
    func test_when_view_appears_with_description__the_Text_view_matches_design_requirements() throws
    {
        // text-align: center, font: Roboto-Regular, size: 14
        // given
        let expectedFont = Font.custom(
            "Roboto-Regular",
            size: mockFrame().height * (14 / idealFrame().height)
        )
        let sut = StatsModalContent.Stat(mockDescription: "Win %")
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: sut.environmentObject(dimensions))
        dimensions.setDimensions(
            width: mockFrame().width,
            height: mockFrame().height,
            screenSize: CGSize(width: mockScreen().width, height: mockScreen().height)
        )
        let displayedText = try sut.inspect().vStack().text(1)
        
        // then
        XCTAssertEqual(try expectedFont.name(), try displayedText.attributes().font().name())
        XCTAssertEqual(try expectedFont.size(), try displayedText.attributes().font().size())
        XCTAssertEqual(.ui.onSurface, try displayedText.attributes().foregroundColor())
        XCTAssertEqual(.center, try displayedText.multilineTextAlignment())
    }
}

extension StatsModalContent.Stat
{
    init(mockHeadline: String = "", mockDescription: String = "")
    {
        self.init(headline: mockHeadline, description: mockDescription)
    }
}
