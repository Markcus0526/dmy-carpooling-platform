//
//  Cus_grabLongWayOrderViewController.h
//  BJPinChe
//
//  Created by CKK on 14-8-25.
//  Copyright (c) 2014年 KimOC. All rights reserved.
//
/**
 *  长途抢座界面
 *
 *  @param weak      <#weak description#>
 *  @param nonatomic <#nonatomic description#>
 *
 *  @return <#return value description#>
 */
#import "SuperViewController.h"
#import "LongWayOrderSvcMgr.h"
#import "PasswordCheckModal.h"
#import "Cus_NotEnoughViewController.h"
@interface Cus_grabLongWayOrderViewController : SuperViewController<UITableViewDataSource,UITableViewDelegate,UITextFieldDelegate,UIPickerViewDataSource,UIPickerViewDelegate,LongWayOrderSvcDelegate, pwdPopupDelegate,Cus_NotEnoughViewDelegate>

@property (weak, nonatomic) IBOutlet UIView *textFieldView;

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UITextField *startCityTextField;
@property (weak, nonatomic) IBOutlet UITextField *endCityTextField;
@property (weak, nonatomic) IBOutlet UIView *datePickerView;
@property (weak, nonatomic) IBOutlet UIPickerView *picker;
//pickerView数据源
@property (strong, nonatomic) NSMutableArray * pickerArray;

- (IBAction)OnDatePickerCancelClick:(id)sender;
- (IBAction)OnDatePickerSureClick:(id)sender;
- (IBAction)onSearchButtonClick:(id)sender;

- (IBAction)onBackClick:(id)sender;

- (void)onAcceptClickWithWithLongOrderInfo:(STAcceptableLongOrderInfo *) longOrderInfo AndSeatsCount:(int)count;
-(void)OnSelectNumberClickWithNumber:(int)seats WithLabel:(UILabel *)selectNumLabel;


@end
