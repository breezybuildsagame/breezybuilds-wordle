//
//  AppSheetViewHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 5/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import XCTest
import ViewInspector
import SwiftUI
import shared
@testable import iosApp

final class AppSheetViewHandlerTests: XCTestCase
{
    func test_when_setUp_invoked__AppSheet_setHandler_method_is_invoked_passing_in_self()
    {
        // given
        let appSheetMock = AppSheetCommonMock()
        let sut = AppSheetViewHandler(appSheet: appSheetMock)
        appSheetMock.setHandlerPassedInNewHandler = nil
        
        // when
        sut.setUp()
        
        // then
        XCTAssertNotNil(appSheetMock.setHandlerPassedInNewHandler)
    }
    
    func test_when_onSheetShouldShow_invoked__expected_view_is_displayed() throws
    {
        // given
        let appSheetMock = AppSheetCommonMock()
        let appSheetMockContent = appSheetMock.contentToReturn as! HelpSheet
        let sut = AppSheetViewHandler(appSheet: appSheetMock)
        let mockView = MockView(handler: sut)
        let dimensions = SceneDimensions()
        sut.setUp()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView.environmentObject(dimensions))
        dimensions.setDimensions(width: mockScreen().width, height: mockScreen().height)
        sut.onSheetShouldShow(animationDuration: 300)
        
        // then
        let displayedContent = try mockView.inspect().find(HelpSheetContent.self)
        XCTAssertNoThrow(try mockView.inspect().find(text: appSheetMockContent.title()))
        for instruction in appSheetMockContent.instructions()
        {
            XCTAssertNoThrow(try mockView.inspect().find(text: instruction.instruction()))
        }
        let expectedExampleViews = appSheetMockContent.examples().map()
        { example in
            HelpSheetContent.Example(
                tiles: example.tiles().map()
                { tile in
                    HelpSheetContent.Tile(
                        letter: String(describing: tile.letter()),
                        state: tile.state()
                    )
                },
                description: example.description_()
            )
        }
        for (index, displayedExampleView) in try displayedContent.actualView().examples.enumerated()
        {
            for (tileIndex, tile) in displayedExampleView.tiles.enumerated()
            {
                XCTAssertEqual(expectedExampleViews[index].tiles[tileIndex].letter, tile.letter)
                XCTAssertEqual(expectedExampleViews[index].tiles[tileIndex].state, tile.state)
            }
            
            XCTAssertEqual(expectedExampleViews[index].description, displayedExampleView.description)
        }
        XCTAssertEqual(appSheetMockContent.footer(), try displayedContent.actualView().footer)
    }
    
    func test_when_onSheetShouldHide_invoked__expected_view_is_displayed() throws
    {
        // given
        let appSheetMock = AppSheetCommonMock()
        let appSheetMockContent = appSheetMock.contentToReturn as! HelpSheet
        let sut = AppSheetViewHandler(appSheet: appSheetMock)
        let mockView = MockView(handler: sut)
        let dimensions = SceneDimensions()
        sut.setUp()
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView.environmentObject(dimensions))
        dimensions.setDimensions(width: mockScreen().width, height: mockScreen().height)
        sut.onSheetShouldShow(animationDuration: 300)
        sut.onSheetShouldHide(animationDuration: 300)
        
        // then
        XCTAssertThrowsError(try mockView.inspect().find(HelpSheetContent.self))
    }
    
    struct MockView: View
    {
        var handler: AppSheetViewHandler
        
        var body: some View
        {
            if (handler.appSheetIsShowing)
            {
                handler.sheetContent()
            }
        }
    }
}
