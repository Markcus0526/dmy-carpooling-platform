//
//  Cus_EvalDriverViewController.h
//  BJPinChe
//
//  Created by Kim Ok Chol on 9/3/14.
//  Copyright (c) 2014 KimOC. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol EvalCusDelegate;

@interface Drv_EvalCustomerViewController : UIViewController<UITextFieldDelegate, UITextViewDelegate>
{
    IBOutlet UIButton *             _radioEvalHigh;
    IBOutlet UIButton *             _radioEvalMedium;
    IBOutlet UIButton *             _radioEvalLow;

    IBOutlet UITextView *           _txtMessage;
    
    IBOutlet UIView *               _vwUsualMsg;
    IBOutlet UIView *               _vwBottom;
    
    IBOutlet UIButton *             _markArrow;
    
    /********************** Member Variable ***********************/
    id                              mParent;
    
    enum EVAL_STATE                 mLevel;
    
    STPassengerInfo *               mDataInfo;
}

@property(strong, nonatomic) id<EvalCusDelegate> delegate;

- (void) initData : (id)parent dataInfo:(STPassengerInfo *)dataInfo;

- (IBAction)onClickedBackground:(id)sender;

- (IBAction)onSelectEvalHigh:(id)sender;
- (IBAction)onSelectEvalMedium:(id)sender;
- (IBAction)onSelectEvalLow:(id)sender;

- (IBAction)onClickedComment1:(id)sender;
- (IBAction)onClickedComment2:(id)sender;
- (IBAction)onClickedComment3:(id)sender;
- (IBAction)onClickedComment4:(id)sender;

- (IBAction)onClickedCancel:(id)sender;
- (IBAction)onClickedSubmit:(id)sender;

- (IBAction)markArrowClick:(id)sender;

@end



// service protocol
@protocol EvalCusDelegate <NSObject>

@optional

- (void) submitComment : (NSString *)message level:(int)level passInfo:(STPassengerInfo *)passInfo;

@end

