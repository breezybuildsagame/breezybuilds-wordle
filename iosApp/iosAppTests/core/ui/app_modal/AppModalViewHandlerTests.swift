//
//  AppModalViewHandlerTests.swift
//  iosAppTests
//
//  Created by Bradley Phillips on 4/9/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI
import ViewInspector
import XCTest
@testable import iosApp
@testable import shared

final class AppModalViewHandlerTests: XCTestCase
{
    func test_when_setUp_invoked__AppModal__setHandler_method_is_invoked_passing_in_self()
    {
        // given
        let mockModal = AppModalCommonMock()
        let sut = AppModalViewHandler(appModal: mockModal)
        
        // when
        sut.setUp()
        
        // then
        XCTAssertNotNil(mockModal.setHandlerMethodPassedInNewHandler)
    }
    
    func test_when_onModalShouldShow_invoked__expected_view_is_displayed() throws
    {
        //given
        let mockModal = AppModalCommonMock()
        let expectedModalContent = MockSharedModalHelper().mockStatsModal()
        let sut = AppModalViewHandler(appModal: mockModal)
        mockModal.contentToReturn = expectedModalContent
        let mockView = MockView(handler: sut)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView.environmentObject(SceneDimensions()))
        mockModal.handler()?.onModalShouldShow(animationDuration: 0)
        let displayedMockView = try mockView.inspect().find(MockView.self)
        
        // then
        XCTAssertNoThrow(try displayedMockView.zStack().find(StatsModalContent.self))
    }
    
    func test_when_onModalShouldHide_invoked__expected_view_is_not_displayed() throws
    {
        //given
        let mockModal = AppModalCommonMock()
        let expectedModalContent = MockSharedModalHelper().mockStatsModal()
        let sut = AppModalViewHandler(appModal: mockModal)
        mockModal.contentToReturn = expectedModalContent
        let mockView = MockView(handler: sut)
        
        // when
        defer { ViewHosting.expel() }
        ViewHosting.host(view: mockView.environmentObject(SceneDimensions()))
        mockModal.handler()?.onModalShouldShow(animationDuration: 0)
        mockModal.handler()?.onModalShouldHide(animationDuration: 0)
        let displayedMockView = try mockView.inspect().find(MockView.self)
        
        // then
        XCTAssertThrowsError(try displayedMockView.zStack().find(StatsModalContent.self))
    }
    
    struct MockView: View
    {
        @ObservedObject var handler: AppModalViewHandler
        
        var body: some View
        {
            ZStack
            {
                if (handler.appModalIsShowing) { handler.modalContent() }
            }
            .onAppear { handler.setUp() }
        }
    }
    
    class MockSharedModalHelper
    {
        var closeButtonClicked = false
        var playAgainButtonClicked = false
        
        var statsToReturn: [Stat] = [
            Stat(headline: "10", description: "Something"),
            Stat(headline: "20", description: "Cool!")
        ]
        var guessDistributionToReturn: GuessDistribution = GuessDistribution(
            title: "Guess Distribution",
            rows: [
                GuessDistribution.Row(round: 1, correctGuessesCount: 2),
                GuessDistribution.Row(round: 2, correctGuessesCount: 3)
            ]
        )
        
        func mockStatsModal() -> StatsModal
        {
            var closeButton = Button { _ in self.closeButtonClicked = true }
            var playAgainButton = Button { _ in self.playAgainButtonClicked = true }
            
            return StatsModal(
                closeButton: closeButton,
                stats: statsToReturn,
                guessDistribution: guessDistributionToReturn,
                playAgainButton: playAgainButton
            )
        }
        
        class Button: ButtonRepresentable
        {
            var mockResourceId: String?
            var mockLabel: String?
            var onClick: (Error?) -> Void
            
            init(mockResourceId: String? = nil, mockLabel: String? = nil, onClick: @escaping (Error?) -> Void)
            {
                self.mockResourceId = mockResourceId
                self.mockLabel = mockLabel
                self.onClick = onClick
            }
            
            func click(completionHandler: @escaping (Error?) -> Void) { onClick(nil) }
            func imageResourceId() -> String? { mockResourceId }
            func label() -> String? { mockLabel }
        }
    }
}
