//
//  Cus_SetMidPointViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 8/30/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "AddrSelectorViewController.h"
#import "iflyMSC/IFlyRecognizerViewDelegate.h"

@protocol MidPointSelDelegate;

@interface Cus_SetMidPointViewController : SuperViewController <AddrSelDelegate, IFlyRecognizerViewDelegate>
{
    IBOutlet UILabel *              _lblAddress1;
    IBOutlet UILabel *              _lblAddress2;
    IBOutlet UILabel *              _lblAddress3;
    IBOutlet UILabel *              _lblAddress4;
    
    STBaiduAddrInfo *               mAddress1;
    STBaiduAddrInfo *               mAddress2;
    STBaiduAddrInfo *               mAddress3;
    STBaiduAddrInfo *               mAddress4;
    
    IFlyRecognizerView *                iflyRecognizerView;
    NSInteger                           mSelectedVoiceTag;
}

@property(strong, nonatomic) id<MidPointSelDelegate> delegate;

@property (nonatomic, assign) NSInteger mParentTag;

@property (nonatomic,strong)NSMutableArray *mitPointArry;

- (IBAction)onClickedBack:(id)sender;
- (IBAction)onClickedConfirm:(id)sender;
- (IBAction)onClickedAddr1:(id)sender;
- (IBAction)onClickedAddr2:(id)sender;
- (IBAction)onClickedAddr3:(id)sender;
- (IBAction)onClickedAddr4:(id)sender;
- (IBAction)onClickedAddr1Voice:(id)sender;
- (IBAction)onClickedAddr2Voice:(id)sender;
- (IBAction)onClickedAddr3Voice:(id)sender;
- (IBAction)onClickedAddr4Voice:(id)sender;
- (IBAction)onClickedRemoveAddr1:(id)sender;
- (IBAction)onClickedRemoveAddr2:(id)sender;
- (IBAction)onClickedRemoveAddr3:(id)sender;
- (IBAction)onClickedRemoveAddr4:(id)sender;



@end



// address selector protocol
@protocol MidPointSelDelegate <NSObject>

- (void) selectedMidPoints : (NSMutableArray *)midPoints parentTag:(NSInteger)parentTag;

@end

